package webserver.handler;

import lombok.extern.slf4j.Slf4j;
import webserver.exception.HandlerInvocationError;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class WrappedHandler {
    private final Object controller;
    private final Method method;
    private final Map<Integer, String> paramIndexToName;

    public WrappedHandler(Object controller, Method method, Map<Integer, String> paramIndexToName) {
        this.controller = controller;
        this.method = method;
        this.paramIndexToName = paramIndexToName;
    }

    public ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            return (ModelAndView) method.invoke(controller, rearrangeArgs(httpRequest, httpResponse));
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new HandlerInvocationError();
        }
    }

    private Object[] rearrangeArgs(HttpRequest httpRequest, HttpResponse httpResponse) {
        Object[] args = new Object[method.getParameterTypes().length];
        for (int nthParam = 0; nthParam < method.getParameterTypes().length; nthParam++) {
            if (method.getParameterTypes()[nthParam] == HttpRequest.class) {
                args[nthParam] = httpRequest;
            }
            if (method.getParameterTypes()[nthParam] == HttpResponse.class) {
                args[nthParam] = httpResponse;
            }
            if (ParsedParams.SUPPORTED_TYPE.contains(method.getParameterTypes()[nthParam])) {
                args[nthParam] = httpRequest.getParsedParams().get(paramIndexToName.get(nthParam));
            }
        }
        return args;
    }
}
