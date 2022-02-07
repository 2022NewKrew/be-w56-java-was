package controller;

import http.HttpMethod;
import http.Request;
import http.RequestType;
import model.User;
import user.controller.UserController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ControllerManager {
    private static Map<RequestType, Function<Request, String>> map = new HashMap<>();

    //mapping path to method.
    static{
        //@PostMapping(value = "/user/create") 를 UserController클래스 execute라는 static method를 매핑해줌.
        map.put(new RequestType(HttpMethod.POST, "/user/create"), UserController::createUser);
        map.put(new RequestType(HttpMethod.POST, "/user/login/check"), UserController::loginUser);

    }


    public static String matchController(Request request){
        RequestType key = new RequestType(request.getMethod(), request.getPath());
        if(map.get(key) != null){
            return map.get(key).apply(request);
        }

        return request.getPath();
    }
}
