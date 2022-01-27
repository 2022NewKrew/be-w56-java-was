package webserver.processor.handler.controller;

import http.HttpRequest;
import http.HttpResponse;
import webserver.HttpFactory;
import webserver.http.HttpEntityConverter;
import webserver.http.RequestEntity;
import webserver.http.ResponseEntity;
import webserver.processor.handler.Handler;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerControllerMappingAdapter implements Handler {

    private List<ControllerMethod> controllerMethods;
    private HttpEntityConverter httpEntityConverter;

    public HandlerControllerMappingAdapter(List<Controller> controllers) {
        List<ControllerMethod> controllerMethods = new ArrayList<>();
        for(Controller controller : controllers) {
            controllerMethods.addAll(getControllerMethods(controller));
        }
        this.controllerMethods = controllerMethods;
        this.httpEntityConverter = HttpFactory.httpEntityConverter();
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return getSupportsMethod(httpRequest) != null;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        ControllerMethod controllerMethod = getSupportsMethod(httpRequest);
        Type methodArgumentInnerType = controllerMethod.getMethodArgumentGenericInnerType();
        RequestEntity<?> requestEntity = httpEntityConverter.convertRequest(httpRequest, methodArgumentInnerType);
        ResponseEntity<?> responseEntity = controllerMethod.handle(requestEntity);
        return httpEntityConverter.convertResponse(responseEntity);
    }

    private ControllerMethod getSupportsMethod(HttpRequest request) {
        for(ControllerMethod controllerMethod : controllerMethods) {
            if(controllerMethod.supports(request)) {
                return controllerMethod;
            }
        }
        return null;
    }

    private List<ControllerMethod> getControllerMethods(Controller controller) {
        Class<? extends Controller> clazz = controller.getClass();
        Method[] methods = clazz.getMethods();

        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Request.class))
                .map(method -> new ControllerMethod(controller, method, method.getAnnotation(Request.class)))
                .collect(Collectors.toList());
    }
}
