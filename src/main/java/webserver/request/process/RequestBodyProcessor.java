package webserver.request.process;

import annotation.RequestBody;
import webserver.http.request.HttpRequest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class RequestBodyProcessor {

    public static Object process(HttpRequest httpRequest, Parameter parameter)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        if (parameter.getAnnotation(RequestBody.class) == null) {
            return null;
        }
        for (Constructor<?> constructor : parameter.getType().getDeclaredConstructors()) {
            return findConstructor(constructor, parameter, httpRequest);
        }
        return null;
    }

    private static Object findConstructor(Constructor<?> constructor, Parameter parameter, HttpRequest httpRequest)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        if (constructor.getParameters().length == httpRequest.bodyValueCount()) {
            return requestBodyInit(constructor, parameter, httpRequest);
        }
        return null;
    }

    private static Object requestBodyInit(Constructor<?> constructor, Parameter parameter, HttpRequest httpRequest)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        List<Object> objects = new ArrayList<>();
        for (String name : parameter.getAnnotation(RequestBody.class).names()) {
            objects.add(httpRequest.getBodyAttribute(name));
        }
        return constructor.newInstance(objects.toArray());
    }
}
