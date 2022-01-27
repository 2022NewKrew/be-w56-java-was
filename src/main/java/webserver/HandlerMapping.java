package webserver;


import com.google.common.reflect.ClassPath;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.enums.HttpMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class HandlerMapping {
    public static class Pair {
        Class<?> clazz;
        Method method;

        Pair(Class<?> clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Method getMethod() {
            return method;
        }
    }

    private static final Map<String, Pair> POST_METHODS = registerPostMethods();
    private static final Map<String, Pair> GET_METHODS = registerGetMethods();

    private static Map<String, Pair> registerPostMethods() {
        Map<String, Pair> getMethods = new HashMap<>();
        Set<Class<?>> classes = findAllClassesInPackage("controller");

        for (Class<?> clazz : classes) {
            getMethods.putAll(Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(PostMapping.class))
                    .collect(HashMap::new,
                            (map, m) -> map.put(m.getAnnotation(PostMapping.class).value(), new Pair(clazz, m)),
                            HashMap::putAll));
        }

        return getMethods;
    }

    private static Map<String, Pair> registerGetMethods() {
        Map<String, Pair> postMethods = new HashMap<>();
        Set<Class<?>> classes = findAllClassesInPackage("controller");

        for (Class<?> clazz : classes) {
            postMethods.putAll(Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(GetMapping.class))
                    .collect(HashMap::new,
                            (map, m) -> map.put(m.getAnnotation(GetMapping.class).value(), new Pair(clazz, m)),
                            HashMap::putAll));
        }

        return postMethods;
    }

    /**
     * Refer to <br>
     * {@link https://github.dev/kakao-2022/be-w56-java-was/blob/ShinjoWang/src/main/java/webserver/RequestMapper.java}
     */
    private static Set<Class<?>>findAllClassesInPackage(String packageName) {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream()
                    .filter(classInfo -> classInfo.getPackageName().equalsIgnoreCase(packageName))
                    .map(ClassPath.ClassInfo::load)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    public static Pair findMethod(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();

        if (httpRequest.getMethod() == HttpMethod.POST)
            return POST_METHODS.get(uri);
        return GET_METHODS.get(uri);
    }
}
