package webserver;

import controller.Controller;
import controller.FrontController;
import controller.LoginController;
import controller.MemoController;
import controller.RegisterController;
import controller.UsersController;
import db.UserRepository;
import httpmodel.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import service.UserService;

public class RequestMapping {

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();
    private static final String FRONT = "front";

    static {
        UserService userService = new UserService(new UserRepository());
        CONTROLLERS.put("/users/login", new LoginController(userService));
        CONTROLLERS.put(FRONT, new FrontController());
        CONTROLLERS.put("/users", new RegisterController(userService));
        CONTROLLERS.put("/users/list", new UsersController(userService));
        CONTROLLERS.put("/qna/form", new MemoController());
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
