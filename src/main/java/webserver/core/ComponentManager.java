package webserver.core;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import webserver.annotations.Bean;
import webserver.annotations.Component;
import webserver.annotations.Primary;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ComponentManager {

    private final String ANNOTATION_PACKAGE = "webserver.annotations";
    private final String PROJECT_PACKAGE = "myspring";

    private static ComponentManager componentManager;

    private Map<Class<?>, Object> objectMap;

    private Map<Class<?>, Set<Class<?>>> interfaceMap;

    private Map<Class<?>, Object> classMap;

    private ComponentManager() {
        objectMap = new HashMap<>();
        interfaceMap = new HashMap<>();
        classMap = new HashMap<>();
    }

    public static ComponentManager getInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (componentManager == null) {
            componentManager = new ComponentManager();
            componentManager.registryAllComponents();
        }
        return componentManager;
    }

    public static void registryComponent(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (componentManager == null) throw new IllegalAccessException("ComponentManager Not initialize!");
        componentManager.updateObjectMapByClazz(clazz);
    }

    public static void registryComponentByObject(Object obj) throws IllegalAccessException {
        if (componentManager == null) throw new IllegalAccessException("ComponentManager Not initialize!");
        componentManager.objectMap.put(obj.getClass(), obj);
    }

    private void registryAllComponents() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        updateAllComponents(new Reflections(ANNOTATION_PACKAGE,PROJECT_PACKAGE), Component.class);
        updateAllBeans(new Reflections(ANNOTATION_PACKAGE,PROJECT_PACKAGE,  Scanners.MethodsAnnotated), Bean.class);
        updateAllObjects();
    }

    private void updateAllComponents(Reflections componentReflector, Class<?> annotationClass) {
        Set<Class<?>> componentclazzs = componentReflector.getTypesAnnotatedWith((Class<Annotation>)annotationClass);
        for(Class<?> componentclazz: componentclazzs) {
            if (componentclazz.isAnnotation()) updateAllComponents(componentReflector, componentclazz);
            if (!componentclazz.isAnnotation() && !componentclazz.isInterface()) {
                classMap.put(componentclazz, componentclazz);
                updateInterfaceMapByClazz(componentclazz);
            }
        }
    }

    private void updateAllBeans(Reflections methodReflector, Class<?> annotationClass) {
        Set<Method> methods = methodReflector.getMethodsAnnotatedWith((Class<Annotation>)annotationClass);
        for(Method method: methods) {
            classMap.put(method.getReturnType(), method);
            updateInterfaceMapByClazz(method.getReturnType());
        }
    }

    private void updateInterfaceMapByClazz(Class<?> clazz){
        for(Class<?> interfaze : clazz.getInterfaces()) {
            if (!interfaceMap.containsKey(interfaze)) interfaceMap.put(interfaze, new HashSet<>());
            Set<Class<?>> s = interfaceMap.get(interfaze);
            s.add(clazz);
        }
    }

    private Class<?> getClazzByInterface(Class<?> interfaze) {
        if (!interfaceMap.containsKey(interfaze))
            throw new IllegalArgumentException(String.format("Invaild Interfaze %s", interfaze.getName()));
        Set<Class<?>> s = interfaceMap.get(interfaze);
        Iterator<Class<?>> iter = s.iterator();
        if (s.size() == 1) return iter.next();
        while(iter.hasNext()) {
            Class<?> clazz = iter.next();
            if (clazz.getAnnotation(Primary.class) != null) return clazz;
        }
        throw new IllegalArgumentException("Confuse implements of Interface!");
    }

    private void updateAllObjects() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for(Class<?> clazz: classMap.keySet()) {
            updateObjectMapByClazz(clazz);
        }
    }

    private void updateObjectMapByClazz(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object v = classMap.get(clazz);
        Object object = null;
        if (v instanceof Class<?>) object = getComponentInstance((Class<?>)v);
        if (v instanceof Method) object = getBeanInstance((Method)v);
        objectMap.put(clazz, object);
    }

    private Object getComponentInstance(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor constructor = constructors[0];
        List<Object> args = new ArrayList<>();
        for(Parameter parameter : constructor.getParameters()) {
            Class<?> targetClazz = parameter.getType();
            if (targetClazz.isInterface()) targetClazz = getClazzByInterface(targetClazz);
            if (!objectMap.containsKey(targetClazz)) updateObjectMapByClazz(targetClazz);
            args.add(objectMap.get(targetClazz));
        }
        return constructor.newInstance(args.toArray());
    }

    private Object getBeanInstance(Method method) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!objectMap.containsKey(method.getDeclaringClass())) updateObjectMapByClazz(method.getDeclaringClass());
        List<Object> args = new ArrayList<>();
        for(Parameter parameter : method.getParameters()) {
            Class<?> targetClazz = parameter.getType();
            if (targetClazz.isInterface()) targetClazz = getClazzByInterface(targetClazz);
            if (!objectMap.containsKey(targetClazz)) updateObjectMapByClazz(targetClazz);
            args.add(objectMap.get(targetClazz));
        }
        return method.invoke(objectMap.get(method.getDeclaringClass()), args.toArray());
    }

    public Object getObjectByClazz(Class<?> clazz) {
        if (clazz.isInterface()) clazz = getClazzByInterface(clazz);
        if (!objectMap.containsKey(clazz)) throw new IllegalArgumentException(String.format("Invalid clazz! %s", clazz.getName()));
        return objectMap.get(clazz);
    }

}
