package webserver;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SingletonBeanRegistry {
    private static final Map<String, Object> beans = new ConcurrentHashMap<>();

    public static void registerBean(String name, Object object) {
        beans.put(name, object);
    }

    public static Object getBean(String name) {
        return beans.get(name);
    }

    public static Set<String> getBeanNamesForAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(key -> isAnnotationMatch(beans.get(key), annotation))
                .collect(Collectors.toSet());
    }

    private static boolean isAnnotationMatch(Object object, Class<? extends Annotation> annotation) {
        return object.getClass().isAnnotationPresent(annotation);
    }
}
