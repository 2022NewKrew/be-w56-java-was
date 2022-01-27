package common.controller;

import application.controller.UserController;
import webserver.controller.StaticResourceController;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    private static final Map<ControllerType, Controller> mappedController = new HashMap<>();

    static {
        mappedController.put(ControllerType.USER, new UserController());
        mappedController.put(ControllerType.STATIC, new StaticResourceController());
    }

    public static Controller getController(String url) {
        ControllerType controllerType = ControllerType.of(url);
        Controller controller = mappedController.get(controllerType);
        if (controller != null) {
            return controller;
        }
        return mappedController.get(ControllerType.STATIC);
    }
}
