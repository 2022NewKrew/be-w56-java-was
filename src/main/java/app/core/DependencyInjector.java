package app.core;

import app.core.annotation.Autowired;
import app.core.annotation.components.Component;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class DependencyInjector {

    private DependencyInjector() {
    }

    public static Map<String, Object> inject() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Set<Class<?>> s = findAllClassesUsingReflectionsLibrary("app");
        for (Class<?> clazz : s.toArray(new Class[0])) {
            String name = clazz.getName();
            if (!isComponent(clazz)) continue;
            map.putIfAbsent(name, makeObject(map, clazz));
        }
        return map;
    }

    private static boolean isComponent(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        Optional<Annotation> annotationOptional = Arrays.stream(annotations)
                .distinct()
                .filter(a -> (a.annotationType().getAnnotation(Component.class) != null))
                .findAny();
        return annotationOptional.isPresent();
    }

    private static Optional<Constructor<?>> getAutowiredConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getConstructors())
                .distinct()
                .filter(c -> c.getAnnotation(Autowired.class) != null)
                .findAny();
    }

    private static Object makeObject(Map<String, Object> map, Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 1. @Autowired 생성자를 읽는다.
        Optional<Constructor<?>> optionalConstructor = getAutowiredConstructor(clazz);
        // 1-1. 없다면 주입받을 인자가 없다고 생각하고 그냥 생성.
        if (optionalConstructor.isEmpty())
            return clazz.getConstructor().newInstance();
        // 2. 파라미터를 꺼낸다.
        List<Object> paramList = new ArrayList<>();
        Constructor<?> constructor = optionalConstructor.get();
        Parameter[] parameters = constructor.getParameters();
        // 3. 파라미터를 재귀적으로 생성
        for (Parameter parameter : parameters) {
            Class<?> nextClazz = parameter.getType();
            String parameterName = nextClazz.getName();
            map.putIfAbsent(parameterName, makeObject(map, nextClazz));
            paramList.add(map.get(parameterName));
        }
        // 4. 객체를 생성하여 반환
        return constructor.newInstance(paramList.toArray());
    }

    public static Set<Class<?>> findAllClassesUsingReflectionsLibrary(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

}
