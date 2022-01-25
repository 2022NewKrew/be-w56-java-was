package webserver;

import controller.AbstractController;
import controller.Controller;
import controller.StaticController;
import java.util.List;

public class HandlerMapping {

    private static final AbstractController defaultController = StaticController.getInstance();

    private static HandlerMapping instance;

    private final List<AbstractController> controllers;

    private HandlerMapping(List<AbstractController> controllers) {
        this.controllers = controllers;
    }

    public static HandlerMapping getInstance() {
        if (instance == null) {
            instance = new HandlerMapping(List.of(StaticController.getInstance()));
        }
        return instance;
    }

    public Controller getController(String path) {
        return controllers.stream()
                .filter(controller -> controller.match(path))
                .findAny()
                .orElse(defaultController);
    }
}
