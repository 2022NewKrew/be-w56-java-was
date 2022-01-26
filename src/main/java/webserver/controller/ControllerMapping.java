package webserver.controller;

import util.request.MethodType;

import java.util.List;
import java.util.Optional;

public class ControllerMapping {
    private final List<Controller<?>> controllers = List.of(
            new StaticController(),
            new UserController()
    );

    public Optional<Controller<?>> getController(MethodType methodType, String url){
        return controllers.stream()
                .filter(controller -> controller.supports(methodType, url))
                .findFirst();
    }
}
