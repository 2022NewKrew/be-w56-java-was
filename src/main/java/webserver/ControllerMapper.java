package webserver;

import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.UserController;
import controller.DefaultController;

public final class ControllerMapper {
    private static final String USER_CONTROLLER = "/users(.*)";
    private static final String DEFAULT_CONTROLLER = "/default(.*)";
    private static final Map<String, Controller> controllerMap;

    static {
        controllerMap = new HashMap<>();

        controllerMap.put(DEFAULT_CONTROLLER, new DefaultController());
        controllerMap.put(USER_CONTROLLER, new UserController());
    }

    private ControllerMapper() {}

    public static Controller getController(String url) {
        return controllerMap.get(
                controllerMap
                        .keySet().stream().filter(key -> url.matches(key))
                        .findFirst().orElse(DEFAULT_CONTROLLER)
        );
    }
}
