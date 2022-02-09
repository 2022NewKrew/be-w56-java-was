package webserver;

import com.google.common.reflect.ClassPath;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class DIContainer {
    private Map<String, Object> beans = new HashMap<>();
    private Set<Class<?>> classes;

    public DIContainer() {
        autowire();
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    /**
     * Create beans and inject dependencies
     */
    private void autowire() {
        // Find all classes which has Component annotation above
        classes = findAllClassesInPackage(new String[]{"controller", "service", "repository"});

        try {
            classes.forEach(this::createInstance);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(System.out::println);
        }
    }

    /**
     * Create bean
     *
     * @param clazz target class to be instantiated
     */
    private void createInstance(Class<?> clazz) {
        // If target class is an interface, find implement class
        Class<?> implClazz = clazz.isInterface() ? findImplementationOf(clazz) : clazz;

        // Return if it has already been instantiated
        if (beans.get(implClazz.getSimpleName()) != null)
            return;

        // Find constructor which has Autowired annotation above
        Constructor<?> constructor = Arrays.stream(implClazz.getConstructors())
                .filter(cons -> cons.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElse(null);

        if (constructor == null)
            return;

        // Create parameter list of the constructor
        List<Object> params = new ArrayList<>();
        for (Class<?> paramClazz : constructor.getParameterTypes()) {
            createInstance(paramClazz);
            Class<?> implParamClazz = paramClazz.isInterface() ? findImplementationOf(paramClazz) : paramClazz;
            params.add(beans.get(implParamClazz.getSimpleName()));
        }

        try {
            Object newObject = params.size() != 0 ?
                    constructor.newInstance(params.toArray()) : constructor.newInstance();
            beans.put(implClazz.getSimpleName(), newObject);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(System.out::println);
        }
    }

    /**
     * Find the implementation class of the given class
     *
     * @param interfaceClass class info of the interface class
     * @return class info of the implementation class
     */
    private Class<?> findImplementationOf(Class<?> interfaceClass) {
        return classes.stream()
                .filter(beanClass ->
                        Arrays.stream(beanClass.getInterfaces())
                                .map(Class::getSimpleName)
                                .anyMatch(name -> name.equals(interfaceClass.getSimpleName())))
                .findFirst().get();
    }

    private Set<Class<?>> findAllClassesInPackage(String[] packageNames) {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream()
                    .filter(classInfo -> Arrays.stream(packageNames)
                            .anyMatch(packageName -> classInfo.getPackageName().equalsIgnoreCase(packageName)))
                    .map(ClassPath.ClassInfo::load)
                    .filter(clazz -> clazz.isAnnotationPresent(Component.class))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            return new HashSet<>();
        }
    }
}