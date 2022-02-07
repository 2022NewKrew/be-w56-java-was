package webserver;

import controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static final Map<String, Controller> mappingController = new HashMap<>();

    static {
        mappingController.put("/", new StaticController("/index.html"));
        mappingController.put("/users/form", new StaticController("/user/form.html"));
        mappingController.put("/users/create", new CreateUserController());
        mappingController.put("/users/login", new LoginController());
        mappingController.put("/users/loginFail", new StaticController("/user/login_failed.html"));
        mappingController.put("/users/list", new ShowUsersController());
    }

    public static Controller getController(String url) {
        return mappingController.get(url);
    }

}
