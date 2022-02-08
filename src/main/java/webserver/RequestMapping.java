package webserver;

import controller.Controller;
import controller.FrontController;
import controller.LoginController;
import controller.UserController;
import model.Request;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static final String BASIC_URL = "/";
    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();
    private static final String FRONT = "front";

    static {
        final UserService userService = new UserService();
        CONTROLLERS.put("/user/create", new UserController(userService));
        CONTROLLERS.put(FRONT, new FrontController());
        CONTROLLERS.put("/user/login", new LoginController(userService));
    }

    public Controller getController(Request request) {
        if (request.isFile() || request.isEqualUrl(BASIC_URL)) {
            return CONTROLLERS.get(FRONT);
        }

        return CONTROLLERS.entrySet()
                .stream()
                .filter(entry -> request.isEqualUrl(entry.getKey()))
                .findFirst()
                .orElseThrow()
                .getValue();
    }
}
