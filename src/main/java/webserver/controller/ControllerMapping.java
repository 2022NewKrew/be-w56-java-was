package webserver.controller;

import factory.ControllerFactory;
import util.request.HttpRequest;

import java.util.List;
import java.util.Optional;

public class ControllerMapping {
    private final List<Controller<?>> controllers = ControllerFactory.getNormalControllers();
    private final Controller<?> errorController = ControllerFactory.getNotFoundController();

    public Controller<?> getController(HttpRequest httpRequest){
        Optional<Controller<?>> controllerOptional
                = controllers.stream()
                .filter(controller -> controller.supports(httpRequest))
                .findFirst();

        if(controllerOptional.isPresent()){
            return controllerOptional.get();
        }

        return errorController;
    }
}
