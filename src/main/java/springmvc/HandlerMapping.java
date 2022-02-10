package springmvc;

import springmvc.controller.*;
import webserver.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapping {
    private static Map<String, Controller> controllers;

    static {
        controllers = new HashMap<>();
        controllers.put("/", new MemoController());
        controllers.put("/create", new MemoCreateController());
        controllers.put("/user/list", new UserListController());
        controllers.put("/user/create", new UserController());
        controllers.put("/user/login", new LoginController());
    }

    public static Optional<Controller> getController(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        return Optional.ofNullable(controllers.get(path));
    }
}
