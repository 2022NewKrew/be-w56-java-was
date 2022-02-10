package webserver.controller;

import webserver.http.request.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class ControllerCommander {
    private static List<Controller> controllers = new ArrayList<>();

    static {
        controllers.add(new UserController());
        controllers.add(new MainController());
        controllers.add(new LoginController());
    }

    public static Controller findController(HttpRequest req) {
        return controllers.stream()
                .filter(controller -> controller.isSupport(req)).findFirst().orElse(null);
    }
}
