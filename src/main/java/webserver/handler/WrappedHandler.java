package webserver.handler;

import lombok.extern.slf4j.Slf4j;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class WrappedHandler {
    private final Object controller;
    private final HandlerParamParser handlerParamParser;
    private final Method method;

    public WrappedHandler(Object controller, Method method, HandlerParamParser handlerParamParser) {
        this.controller = controller;
        this.method = method;
        this.handlerParamParser = handlerParamParser;
    }

    public ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        log.debug("Handling request in wrapped handler");
        try {

            method.invoke(controller,
                          methodToArrange(method, httpRequest, httpResponse, handlerParamParser.parse(httpRequest)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private Object[] methodToArrange(Method method, HttpRequest httpRequest, HttpResponse httpResponse,
                                     Object[] params) {
        // Inspect method signature
        // Arrange arguments accordingly
        return null;
    }
}
