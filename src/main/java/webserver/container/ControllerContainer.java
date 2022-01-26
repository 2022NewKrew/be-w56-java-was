package webserver.container;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import webserver.dispatcher.dynamic.bind.annotation.RestController;
import webserver.exception.ContainerInitializationException;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

/**
 * Singleton
 */
public class ControllerContainer {

    private static final String DEFAULT_COMPONENT_SCAN_DIR = "webserver.api";

    private static final ControllerContainer INSTANCE = new ControllerContainer();

    public static ControllerContainer getInstance() {
        return INSTANCE;
    }

    private ControllerContainer() {
        scanAndCreateRestControllers();
    }

    private Map<Class<?>, Object> restControllers;

    private void scanAndCreateRestControllers() {
        Reflections reflections = new Reflections(DEFAULT_COMPONENT_SCAN_DIR, Scanners.TypesAnnotated);
        Set<Class<?>> types =  reflections.getTypesAnnotatedWith(RestController.class);
        for(Class<?> type : types) {
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
        return restControllers.get(type);
    }
}
