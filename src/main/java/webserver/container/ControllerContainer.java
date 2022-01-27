package webserver.container;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import webserver.dispatcher.dynamic.bind.annotation.RestController;
import webserver.exception.ContainerInitializationException;
import webserver.exception.NoSuchControllerException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton
 */
public class ControllerContainer {

    private static final String DEFAULT_COMPONENT_SCAN_DIR = "api";

    private static final ControllerContainer INSTANCE = new ControllerContainer();

    public static ControllerContainer getInstance() {
        return INSTANCE;
    }

    private ControllerContainer() {
        scanAndCreateRestControllers();
    }

    private Map<Class<?>, Object> restControllers;

    private void scanAndCreateRestControllers() {
        restControllers = new HashMap<>();
        Reflections reflections = new Reflections(DEFAULT_COMPONENT_SCAN_DIR, Scanners.TypesAnnotated);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(RestController.class);
        for (Class<?> type : types) {
            try {
                Constructor<?> constructor = type.getDeclaredConstructor();
                Object controller = constructor.newInstance();
                restControllers.put(type, controller);
            } catch (Exception e) {
                throw new ContainerInitializationException();
            }
        }
    }

    public Set<Class<?>> getRestControllerTypes() {
        return restControllers.keySet();
    }

    public Object getControllerInstance(Class<?> type) {
        Object controller = restControllers.get(type);
        if (controller == null) {
            throw new NoSuchControllerException();
        }
        return controller;
    }
}
