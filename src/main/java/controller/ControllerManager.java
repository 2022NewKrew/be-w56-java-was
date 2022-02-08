package controller;

import http.HttpMethod;
import http.Request;
import http.RequestType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.controller.UserCreate;
import user.controller.UserLogin;
import webserver.RequestHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ControllerManager {
    private static Map<RequestType, AbstractController> map = new HashMap<>();

    //mapping path to method.
    static{
        map.put(new RequestType(HttpMethod.POST, "/user/create"), new UserCreate());
        map.put(new RequestType(HttpMethod.POST, "/user/login/check"), new UserLogin());
    }


    public static String executeController(Request request){
        RequestType key = new RequestType(request.getMethod(), request.getPath());
        if(map.get(key) != null){
            return map.get(key).execute(request);
        }

        return request.getPath();
    }
}
