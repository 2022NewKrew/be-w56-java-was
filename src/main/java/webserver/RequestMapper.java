package webserver;

import static webserver.AppConfig.appConfig;

import controller.Controller;
import controller.PageController;
import controller.LoginController;
import controller.RegisterController;
import httpmodel.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import service.UserService;

public class RequestMapper {

    private static final String NOT_FOUND = "[ERROR] 404 NOT FOUND";

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();

    static {
        UserService userService = appConfig.userService();

        CONTROLLERS.put(appConfig.HOME, new PageController());
        CONTROLLERS.put("/user/login", new LoginController(userService));
        CONTROLLERS.put("/user/create", new RegisterController(userService));
    }

    public Controller getController(HttpRequest httpRequest) {
        if (httpRequest.isPathFile() || httpRequest.getPath().equals("/")) {
            return CONTROLLERS.get(appConfig.HOME);
        }
        return CONTROLLERS.entrySet()
            .stream()
            .filter(entry -> httpRequest.isPathMatch(entry.getKey()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND))
            .getValue();
    }
}
