package webserver;

import webserver.controller.Controller;
import webserver.controller.StaticController;
import webserver.controller.UserController;

import java.util.List;
import java.util.Optional;

public class ControllerMapping {
    private final List<Controller<?>> controllers = List.of(
            new StaticController(),
            new UserController()
    );

    Optional<Controller<?>> getController(String url){
        return controllers.stream()
                .filter(controller -> controller.supports(url))
                .findFirst();
    }
}
