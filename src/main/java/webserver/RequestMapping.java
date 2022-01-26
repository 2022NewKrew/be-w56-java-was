package webserver;

import controller.Controller;
import controller.FrontController;
import controller.LoginController;
import controller.RegisterController;
import httpmodel.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import service.UserService;

public class RequestMapping {

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();
    private static final String FRONT = "front";

    static {
        UserService userService = new UserService();
        CONTROLLERS.put("/users/login", new LoginController(userService));
        CONTROLLERS.put(FRONT, new FrontController());
        CONTROLLERS.put("/users", new RegisterController(userService));
    }

    public Controller getController(HttpRequest httpRequest) {
        if (httpRequest.isUriFile() || httpRequest.isUriMatch("/")) {
            return CONTROLLERS.get(FRONT);
        }
        return CONTROLLERS.entrySet()
            .stream()
            .filter(entry -> httpRequest.isUriMatch(entry.getKey()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 지원되지 않는 URL입니다."))
            .getValue();
    }
}
