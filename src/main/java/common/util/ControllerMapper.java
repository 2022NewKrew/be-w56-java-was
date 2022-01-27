package common.util;

import application.controller.UserController;
import common.controller.AbstractController;
import common.controller.ControllerType;
import webserver.controller.StaticResourceController;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    private static final Map<ControllerType, AbstractController> mappedController = new HashMap<>();

    static {
        mappedController.put(ControllerType.USER, new UserController());
        mappedController.put(ControllerType.STATIC, new StaticResourceController());
    }

    public static AbstractController getController(String url) {
        String parsed = url.split("/")[1];
        ControllerType controllerType = ControllerType.of(parsed);
        AbstractController controller = mappedController.get(controllerType);
        if (controller != null) {
            return controller;
        }
        return mappedController.get(ControllerType.STATIC);
    }
}
