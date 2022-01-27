package framework.handler;

import controller.UserController;
import util.HttpRequest;
import util.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class RequestMappingHandler extends Handler{

    private final List<HandlerMethod> handlerMethods;

    public RequestMappingHandler(List<HandlerMethod> handlerMethods) {
        this.handlerMethods = handlerMethods;
    }

    @Override
    public String handle(HttpRequest req, HttpResponse res) {
        HandlerMethod method = lookupHandlerMethod(req).get();
        Object returnValue = null;
        try {
            returnValue = method.getHandleMethod().invoke(method.getBean(), req, res);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (String) returnValue;
    }

    @Override
    public boolean isSupport(HttpRequest req) {
        return lookupHandlerMethod(req).isPresent();
    }

    private Optional<HandlerMethod> lookupHandlerMethod(HttpRequest req) {
        return handlerMethods.stream()
                .filter(h -> h.matchRequest(req))
                .findAny();
    }
}
