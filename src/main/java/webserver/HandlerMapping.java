package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static Map<String, Method> requestMap;

    static {
        requestMap = new HashMap<>();
    }

    public static void initRequestMapping(Class<?> clazz) {

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String[] paths = requestMapping.value();
                for (String path : paths) {
                    requestMap.put(path, method);
                    log.info("[\"" + path + "\"]" + method.getName());
                }
            }
        }
    }

    public static Method getMethod(String uriString) {
        return requestMap.get(uriString);
    }

}
