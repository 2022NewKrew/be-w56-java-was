package controller;

import webserver.HttpRequest;

import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControllerCommander {
    private static List<Controller> controllers = new ArrayList<>();

    static {
        controllers.add(new UserController());
        controllers.add(new MainController());
    }

    public static Controller findController(HttpRequest req) throws NoSuchProviderException {
        return controllers.stream()
                .filter(controller -> controller.isSupport(req.getMethod() + req.getRequestUri())).findFirst()
                .orElseThrow(() -> new NoSuchProviderException("404 Error"));
    }
}
