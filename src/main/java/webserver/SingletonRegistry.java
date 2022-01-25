package webserver;

import util.IOUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonRegistry {
    private static final Map<String, Object> singletons = new ConcurrentHashMap<>();

    public static void registerClassesInPackage(String packageName) {
        Set<Class<?>> classes = IOUtils.findAllClassesInPackage(packageName);
        for (Class<?> clazz : classes) {
            try {
                registerSingleton(clazz.getName(), clazz.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerSingleton(String name, Object object) {
        singletons.put(name, object);
    }

    public static Object getSingleton(String name) {
        return singletons.get(name);
    }
}
