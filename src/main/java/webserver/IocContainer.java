package webserver;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.annotation.Bean;

public class IocContainer {
    private static final Logger log = LoggerFactory.getLogger(IocContainer.class);
    private static final Reflections reflections = new Reflections("app");
    private static final Map<String, Object> beans = new ConcurrentHashMap<>();
    private static boolean isClass(Class<?> clazz) {
        return !(clazz.isAnnotation() || clazz.isInterface() || clazz.isEnum());
    }
    private static void registerBeans() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Bean.class);
        for (Class<?> clazz : classes) {
            if(!isClass(clazz)) {
                continue;
            }
            try {
                beans.put(clazz.getName(), clazz.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    static {
        registerBeans();
    }
    public static Set<Class<?>> getAllAnnotatedClass(Class<? extends Annotation> annotationClass) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }
}
