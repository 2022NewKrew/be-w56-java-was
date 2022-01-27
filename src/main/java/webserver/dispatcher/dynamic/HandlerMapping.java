package webserver.dispatcher.dynamic;

import webserver.container.ControllerContainer;
import webserver.dispatcher.dynamic.bind.handler.*;
import webserver.exception.UnSupportedHttpMethodException;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestMethod;
import webserver.request.RequestContext;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton
 */
public class HandlerMapping {

    private static final HandlerMapping INSTANCE = new HandlerMapping();

    public static HandlerMapping getInstance() {
        return INSTANCE;
    }

    private Map<HttpRequestMethod, MethodHandlerMapping> methodHandlerMappings;

    private HandlerMapping() {
        registerMethodHandlerMappings();
        initEachMethodHandlerMappings();
    }

    private void registerMethodHandlerMappings() {
        Map<HttpRequestMethod, MethodHandlerMapping> methodHandlerMappingMap = new EnumMap<>(HttpRequestMethod.class);
        methodHandlerMappingMap.put(HttpRequestMethod.GET, GetHandlerMapping.getInstance());
        methodHandlerMappingMap.put(HttpRequestMethod.POST, PostHandlerMapping.getInstance());
        methodHandlerMappingMap.put(HttpRequestMethod.PUT, PutHandlerMapping.getInstance());
        methodHandlerMappingMap.put(HttpRequestMethod.DELETE, DeleteHandlerMapping.getInstance());
        this.methodHandlerMappings = methodHandlerMappingMap;
    }

    private void initEachMethodHandlerMappings() {
        Set<Class<?>> restControllerTypes = ControllerContainer.getInstance().getRestControllerTypes();
        methodHandlerMappings.entrySet().stream()
                .forEach(entry -> entry.getValue().initMappingTable(restControllerTypes));
    }

    public ClassAndMethod getControllerClassAndMethodForRequest() {
        HttpRequest request = RequestContext.getInstance().getHttpRequest();
        HttpRequestMethod requestMethod = request.getHttpRequestMethod();
        MethodHandlerMapping methodHandlerMapping = methodHandlerMappings.get(requestMethod);
        if (methodHandlerMapping == null) {
            throw new UnSupportedHttpMethodException();
        }
        return methodHandlerMapping.getMethod(request.getUri().getUrl());
    }
}
