package webserver;

import webserver.controller.Controller;
import webserver.controller.StaticController;

import java.util.List;
import java.util.Optional;

public class ControllerMapping {
    private List<Controller> controllers = List.of(
            new StaticController()
    );

    Optional<Controller> getController(String url){
        return controllers.stream()
                .filter(controller -> controller.supports(url))
                .findFirst();
    }
}
