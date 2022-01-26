package handler;

import http.request.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {
    private final Class<?> handlerType;
    private final Method method;

    public HandlerMethod(Class<?> handlerType, Method method) {
        this.handlerType = handlerType;
        this.method = method;
    }

    private Object getHandler() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return handlerType.getConstructor().newInstance();
    }

    public String invoke(HttpRequest httpRequest) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return (String) method.invoke(getHandler(), httpRequest);
    }
}
