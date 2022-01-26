package webserver;

import controller.Controller;
import controller.StaticFileController;
import controller.UserCreateController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ControllerType {

    STATIC_FILE_CONTROLLER("", StaticFileController.getInstance()),
    USER_CREATE_CONTROLLER("/user/create", UserCreateController.getInstance());
    public static final Logger log = LoggerFactory.getLogger(ControllerType.class);
    public static final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

    static {
        for (ControllerType controllerType : ControllerType.values()) {
            controllerMap.put(controllerType.getPath(), controllerType.getController());
        }
    }

    public static Controller getControllerType(String path) {
        return controllerMap.getOrDefault(path, STATIC_FILE_CONTROLLER.getController());
    }

    private final String path;
    private final Controller controller;

    ControllerType(String path, Controller controller) {
        this.path = path;
        this.controller = controller;
    }

    public String getPath() {
        return path;
    }

    public Controller getController() {
        return controller;
    }
}
