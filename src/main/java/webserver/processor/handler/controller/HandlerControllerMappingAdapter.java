package webserver.processor.handler.controller;

import http.HttpRequest;
import http.HttpResponse;
import webserver.processor.handler.Handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerControllerMappingAdapter implements Handler {

    private List<ControllerMethod> controllerMethods;

    public HandlerControllerMappingAdapter(List<Controller> controllers) {
        List<ControllerMethod> controllerMethods = new ArrayList<>();
        for(Controller controller : controllers) {
            controllerMethods.addAll(getControllerMethods(controller));
        }
        this.controllerMethods = controllerMethods;
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return getSupportsMethod(httpRequest) != null;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        return getSupportsMethod(httpRequest).handle(httpRequest);
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
