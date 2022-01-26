package webserver;

import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;
import webserver.exception.ControllerMethodNotFoundException;
import webserver.model.WebHttpRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HandlerMapping {
    private static Map<String, Method> getRequestMap;
    private static Map<String, Method> postRequestMap;
    private static Map<String, Method> putRequestMap;
    private static Map<String, Method> deleteRequestMap;

    static {
        getRequestMap = new HashMap<>();
        postRequestMap = new HashMap<>();
        putRequestMap = new HashMap<>();
        deleteRequestMap = new HashMap<>();
    }

    public static void initRequestMapping(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                RequestMethod requestMethod = requestMapping.method();
                String[] paths = requestMapping.value();
                for (String path : paths) {
                    switch (requestMethod) {
                        case GET:
                            getRequestMap.put(path, method);
                        case POST:
                            postRequestMap.put(path, method);
                        case PUT:
                            putRequestMap.put(path, method);
                        case DELETE:
                            deleteRequestMap.put(path, method);
                    }
                }
            }
        }
    }

    public static Method getControllerMethod(RequestMethod method, String uri) throws ControllerMethodNotFoundException {
        switch (method) {
            case GET:
                return getRequestMap.get(uri);
            case POST:
                return postRequestMap.get(uri);
            case PUT:
                return putRequestMap.get(uri);
            case DELETE:
                return deleteRequestMap.get(uri);
            default:
                throw new ControllerMethodNotFoundException("No controller was found to handle the uri");
        }
    }

    public static boolean isRegistered(WebHttpRequest httpRequest) {
        RequestMethod requestMethod = RequestMethod.valueOf(httpRequest.method());
        String uri = httpRequest.uri().getPath();

        switch (requestMethod) {
            case GET:
                return getRequestMap.containsKey(uri);
            case POST:
                return postRequestMap.containsKey(uri);
            case PUT:
                return putRequestMap.containsKey(uri);
            case DELETE:
                return deleteRequestMap.containsKey(uri);
            default:
                return false;
        }
    }

}
