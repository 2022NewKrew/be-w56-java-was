package controller;

import dto.ControllerDTO;
import http.HttpMethod;
import model.User;
import user.controller.UserController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ControllerManager {
    private static Map<String, Function<ControllerDTO, String>> map = new HashMap<>();

    //mapping path to method.
    static{
        map.put("/user/create", UserController::execute);
    }


    public static String matchController(String path, HttpMethod method, Map<String, String> elements){
        if(map.get(path) != null){
            ControllerDTO controllerDTO = new ControllerDTO(path, elements);
            return map.get(path).apply(controllerDTO);
        }

        return path;
    }
}
