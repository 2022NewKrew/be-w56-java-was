package webserver;

import next.controller.Controller;
import next.controller.CreateUserController;
import next.controller.ListUserController;
import next.controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private final static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new ListUserController());
    }

    public static Controller getController(String requestUrl) {
        return controllers.get(requestUrl);
    }
}
