package controller;

import webserver.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static Map<String, Controller> controllers;

    static {
        controllers = new HashMap<>();
        controllers.put("/user/create", new UserController());
        controllers.put("/user/login", new AuthController());
    }

    public static Controller getController(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        if (controllers.containsKey(path)){
            return controllers.get(path);
        }
        return null;
    }
}
