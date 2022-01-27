package webserver;

import controller.Controller;
import controller.LoginController;
import controller.StaticController;
import controller.JoinController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    private static final Map<String, Controller> requestMap = new HashMap<>(); // concurrent hash map, hash table

    static {
        requestMap.put("/user/create", new JoinController());
        requestMap.put("/user/login", new LoginController());
    }

    public static Controller mapping(String url) {
        Controller controller = requestMap.get(url);
        if (controller == null) {
            controller = new StaticController();
        }
        return controller;
    }
}
