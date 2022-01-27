package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.LoginController;
import controller.StaticController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static final Map<String, Controller> mappingController = new HashMap<>();

    static {
        mappingController.put("/", new StaticController("/index.html"));
        mappingController.put("/error", new StaticController("/error.html"));
        mappingController.put("/users/form", new StaticController("/user/form.html"));
        mappingController.put("/users/create", new CreateUserController());
        mappingController.put("/users/login", new LoginController());
        mappingController.put("/users/loginFail",new StaticController("/user/login_failed.html"));
    }

    public static Controller getController(String url) {
        return mappingController.getOrDefault(url, mappingController.get("/error"));
    }

}
