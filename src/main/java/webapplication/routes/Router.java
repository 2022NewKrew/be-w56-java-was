package webapplication.routes;

import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webapplication.annotations.util.AnnotationHelper;
import webapplication.annotations.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;

public class Router {

    private static final Logger log = LoggerFactory.getLogger(Router.class);

    private static final Map<String, Method> routeMap = initHandlerMethods();

    private static Map<String, Method> initHandlerMethods() {

        Map<String, Method> routeMap = new HashMap<>();

        try {
            String basePackage = "application";
            List<Method> requestHandlers = AnnotationHelper.getMethodsAnnotatedWith(basePackage, RequestMapping.class);
            for(Method requestHandler : requestHandlers) {
                RequestMapping annotation = requestHandler.getAnnotation(RequestMapping.class);
                routeMap.put(RouteUtils.makeKey(annotation.method().getCode(), annotation.path()), requestHandler);
            }
        } catch (Exception ex) {
            log.info("{} : {}", Router.class.getName(), ex.getMessage());
        }

        return routeMap;
    }

    public static Boolean canRoute(HttpRequest httpRequest) {
        return routeMap.containsKey(RouteUtils.makeKey(httpRequest.getMethod().getCode(), httpRequest.getUrl()));
    }

    public static Method getMethod(HttpRequest httpRequest) {
        return routeMap.get(RouteUtils.makeKey(httpRequest.getMethod().getCode(), httpRequest.getUrl()));
    }

}
