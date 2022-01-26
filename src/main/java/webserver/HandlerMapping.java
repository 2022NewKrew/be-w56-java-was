package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;

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

    public static Method getMethod(RequestMethod method, String uriString) {
        switch (method) {
            case GET:
                return getRequestMap.get(uriString);
            case POST:
                return postRequestMap.get(uriString);
            case PUT:
                return putRequestMap.get(uriString);
            case DELETE:
                return deleteRequestMap.get(uriString);
            default:
                return null;
        }
    }

}
