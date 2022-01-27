package webserver;

import controller.Controller;
import controller.LoginController;
import controller.StaticController;
import controller.JoinController;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMapper {

    private static final ConcurrentHashMap<String, Controller> requestMap = new ConcurrentHashMap<>();

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
