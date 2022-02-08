package com.my.was.container.handlermappings;

import com.my.was.container.BeanContainer;
import com.my.was.container.handlermappings.annotation.Controller;
import com.my.was.container.handlermappings.annotation.RequestMapping;
import com.my.was.http.request.HttpMethod;
import com.my.was.http.request.HttpRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private Map<RequestMappingUriAndMethod, Method> requestMappingRegistry = new HashMap<>();

    public RequestMappingHandlerMapping() {
        mappingController(BeanContainer.getInstance().getBeanTypes());
    }

    private void mappingController(Set<Class<?>> controllers) {
        for (Class<?> clazz : controllers) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                mappingRequestMappingMethod(clazz);
            }
        }
    }

    private void mappingRequestMappingMethod(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                requestMappingRegistry.put(new RequestMappingUriAndMethod(annotation.value(),
                        HttpMethod.getHttpMethodByMethodName(annotation.method())), method);
            }
        }
    }

    @Override
    public Object findHandler(HttpRequest httpRequest) {
        return requestMappingRegistry.get(new RequestMappingUriAndMethod(httpRequest.getPath(), httpRequest.getMethod()));
    }
}
