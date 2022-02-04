package webserver;

import http.HttpMethod;
import http.HttpStatus;
import http.request.HttpRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import webserver.annotation.Controller;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;

public class HandlerMapping {

    private static final MappingRegistry mappingRegistry = new MappingRegistry();

    private static HandlerMapping instance;

    private HandlerMapping() {
    }

    public static HandlerMapping getInstance() {
        if (instance == null) {
            instance = new HandlerMapping();
        }
        return instance;
    }

    public ModelAndView invokeHandlerMethod(HttpRequest httpRequest)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Method method = getHandlerInternal(httpRequest);

        if (method == null && httpRequest.getRequestLine().getMethod() != HttpMethod.GET) {
            return ModelAndView.error(HttpStatus.METHOD_NOT_ALLOWED);
        }

        if (method == null) {
            String viewName = httpRequest.getRequestLine().getUri().getPath();
            return ModelAndView.from(viewName);
        }

        Object methodInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        String viewName = (String) method.invoke(methodInstance, httpRequest);

        return ModelAndView.from(viewName);
    }

    private Method getHandlerInternal(HttpRequest httpRequest) {
        String path = httpRequest.getRequestLine().getUri().getPath();
        HttpMethod httpMethod = httpRequest.getRequestLine().getMethod();

        if (!mappingRegistry.containsKey(path, httpMethod)) {
            return null;
        }

        return mappingRegistry.get(path, httpMethod);
    }

    static class Mapping {

        private final String path;
        private final HttpMethod httpMethod;

        Mapping(String path, HttpMethod httpMethod) {
            this.path = path;
            this.httpMethod = httpMethod;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Mapping mapping = (Mapping) o;

            if (!path.equals(mapping.path)) {
                return false;
            }
            return httpMethod == mapping.httpMethod;
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + httpMethod.hashCode();
            return result;
        }
    }

    static class MappingRegistry {

        private static final String CONTROLLER_PACKAGE = "controller";
        private static final Map<Mapping, Method> mappingRegistry = new HashMap<>();

        public MappingRegistry() {
            Set<Class<?>> controllers = new Reflections(CONTROLLER_PACKAGE)
                    .getTypesAnnotatedWith(Controller.class);
            controllers.forEach(MappingRegistry::register);
        }

        private static void register(Class<?> controller) {
            Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(GetMapping.class))
                    .forEach(method -> {
                        String path = method.getAnnotation(GetMapping.class).value();
                        mappingRegistry.put(new Mapping(path, HttpMethod.GET), method);
                    });

            Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(PostMapping.class))
                    .forEach(method -> {
                        String path = method.getAnnotation(PostMapping.class).value();
                        mappingRegistry.put(new Mapping(path, HttpMethod.POST), method);
                    });
        }

        public boolean containsKey(String path, HttpMethod httpMethod) {
            int count = (int) mappingRegistry.keySet().stream()
                    .filter(key -> key.equals(new Mapping(path, httpMethod)))
                    .count();
            return count > 0;
        }

        public Method get(String path, HttpMethod httpMethod) {
            return mappingRegistry.get(new Mapping(path, httpMethod));
        }
    }
}
