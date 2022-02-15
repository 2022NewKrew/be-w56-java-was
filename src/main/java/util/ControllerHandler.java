package util;

import webserver.controller.Controller;
import webserver.controller.DefaultController;
import webserver.controller.UserController;

import java.util.*;

public class ControllerHandler {

    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new UserController());
    }

    private ControllerHandler() {
    }

    public static Controller getController(String url) {
        Controller controller;
        if (controllerMap.containsKey(url))
            controller = controllerMap.get(url);
        else
            controller = new DefaultController();
        return controller;
    }
}
