package webserver.request.process;

import annotation.GetMapping;
import annotation.PostMapping;
import webserver.http.HttpMethod;
import webserver.http.RequestLine;
import webserver.http.Response;
import webserver.http.request.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodProcessor {

    public static <T> Response process(String path, HttpRequest httpRequest, T instance, Response result, Method method)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        HttpMethod httpMethod = HttpMethod.match(httpRequest.getHeaderAttribute(RequestLine.METHOD.name()));

        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null && httpMethod.isSame(HttpMethod.POST) && postMapping.path().equals(path)) {
            result = PostMethodProcessor.process(instance, method, httpRequest);
        }

        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null && httpMethod.isSame(HttpMethod.GET) && getMapping.path().equals(path)) {
            result = GetMethodProcessor.process(instance, method, httpRequest);
        }

        return result;
    }
}
