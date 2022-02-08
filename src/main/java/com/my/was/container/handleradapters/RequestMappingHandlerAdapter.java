package com.my.was.container.handleradapters;

import com.my.was.container.BeanContainer;
import com.my.was.container.handlermappings.annotation.RequestMapping;
import com.my.was.http.request.HttpRequest;
import com.my.was.http.response.HttpResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object handler) {
        if (handler instanceof Method) {
            Method method = (Method) handler;
            if (method.isAnnotationPresent(RequestMapping.class)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void handle(Object handler, HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            Method method = (Method) handler;
            BeanContainer beanContainer = BeanContainer.getInstance();
            method.invoke(beanContainer.getBean(method.getDeclaringClass()), httpRequest, httpResponse);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
