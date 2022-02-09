package webserver.request.process;

import webserver.http.Response;
import webserver.http.request.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostMethodProcessor {

    public static <T> Response process(T instance, Method method, HttpRequest httpRequest)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Object> objects = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            objects.add(RequestBodyProcessor.process(httpRequest, parameter));
            objects.add(CookieProcessor.process(parameter, httpRequest));
        }
        objects = objects.stream().filter(v -> !Objects.isNull(v)).collect(Collectors.toList());
        return (Response) method.invoke(instance, objects.toArray());
    }
}
