package webserver;

import controller.Controller;
import controller.StaticController;
import controller.UserController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    private static final Map<String, Controller> requestMap = new HashMap<>();

    static {
        requestMap.put("/user/create", new UserController());
    }

    public Controller mapping(String url) {
        Controller controller = requestMap.get(url);
        if (controller == null) {
            controller = new StaticController();
        }
        return controller;
    }
}
