package webserver.framwork.core;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import controller.UserController;
import webserver.framwork.http.request.HttpMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class HandlerMapping {
    //나중에 자동으로 가져오도록 변경
    private static final List<Class<?>> CONTROLLERS = Arrays.asList(
            UserController.class
    );
    private static HandlerMapping instance;

    private final Table<HttpMethod, String, Method> handlerMethods = HashBasedTable.create();
    private final Table<HttpMethod, String, Object> handlerInstances = HashBasedTable.create();

    private HandlerMapping() {
    }

    public static HandlerMapping getInstance() {
        if (instance == null) {
            instance = new HandlerMapping();
            instance.init();
        }
        return instance;
    }

    public void init() {
        //
        // attach methods with "RequestMapping" annotation
        //
        try {
            for (Class<?> controllerClass : CONTROLLERS) {
                Method[] methods = controllerClass.getMethods();
                for (Method method : methods) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        handlerMethods.put(requestMapping.method(), requestMapping.value(), method);
                        handlerInstances.put(requestMapping.method(), requestMapping.value(), controllerClass.getConstructor().newInstance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Method getHandlerMethod(HttpMethod method, String url) {
        return handlerMethods.get(method, url);
    }

    public Object getHandlerInstance(HttpMethod method, String url) {
        return handlerInstances.get(method, url);
    }
}
