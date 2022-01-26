package controller;

import http.HttpMethod;
import http.Request;
import model.User;
import user.controller.UserController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ControllerManager {
    private static Map<String, Function<Request, String>> map = new HashMap<>();

    //mapping path to method.
    static{
        map.put("/user/create", UserController::execute);
    }


    public static String matchController(Request request){
        String path = request.getPath();
        if(map.get(path) != null){
            return map.get(path).apply(request);
        }

        return path;
    }
}
