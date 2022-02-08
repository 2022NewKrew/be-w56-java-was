package mvcframework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private Object declaredObject;
    private Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public void handle(HttpRequest request, HttpResponse response) throws Exception {
        try {
            method.invoke(declaredObject, request, response);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error("{} method invoke fail. error message : {}", method, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
