package webserver.method;

import annotation.Controller;
import annotation.RequestMapping;
import http.HttpRequest;
import webserver.HandlerMapping;
import webserver.RequestMappingInfo;
import webserver.SingletonBeanRegistry;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMappingHandlerMapping implements HandlerMapping {
    private final Map<RequestMappingInfo, RequestMappingHandler> registry = new ConcurrentHashMap<>();

    public void initHandlerMethods() {
        Set<String> controllerNames = SingletonBeanRegistry.getBeanNamesForAnnotation(Controller.class);
        for (String controllerName : controllerNames) {
            registerRequestMappingAnnotatedMethod(controllerName);
        }
    }

    private void registerRequestMappingAnnotatedMethod(String controllerName) {
        Object controller = SingletonBeanRegistry.getBean(controllerName);
        Method[] methods = controller.getClass().getDeclaredMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMappingInfo mappingInfo = createRequestMappingInfo(method);
                    RequestMappingHandler handler = createRequestMappingHandler(controllerName, method);
                    registry.put(mappingInfo, handler);
                });
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = element.getAnnotation(RequestMapping.class);
        return new RequestMappingInfo(requestMapping.value(), requestMapping.method());
    }

    private RequestMappingHandler createRequestMappingHandler(String beanName, Method method) {
        return new RequestMappingHandler(beanName, method);
    }

    @Override
    public RequestMappingHandler getHandler(HttpRequest request) {
        RequestMappingInfo lookupRequestMapping = new RequestMappingInfo(request.getUri().getPath(), request.getMethod());
        return registry.get(lookupRequestMapping);
    }
}
