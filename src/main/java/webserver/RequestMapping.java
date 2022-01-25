package webserver;

import controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static final Map<String, Controller> mappingController = new HashMap<>();

    static {
        mappingController.put("/", new DefaultController());
        mappingController.put("/error", new ErrorController());
        mappingController.put("/users/form", new UserFormController());
        mappingController.put("/users/create", new CreateUserController());
    }

    public static Controller getController(String url) {
        return mappingController.getOrDefault(url, mappingController.get("/error"));
    }

}
