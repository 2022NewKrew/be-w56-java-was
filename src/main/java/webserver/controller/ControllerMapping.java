package webserver.controller;

import util.request.HttpRequest;
import webserver.controller.common.StaticController;
import webserver.controller.user.UserJoinController;

import java.util.List;
import java.util.Optional;

public class ControllerMapping {
    private final List<Controller<?>> controllers = List.of(
            new StaticController(),
            new UserJoinController()
    );

    public Optional<Controller<?>> getController(HttpRequest httpRequest){
        return controllers.stream()
                .filter(controller -> controller.supports(httpRequest))
                .findFirst();
    }
}
