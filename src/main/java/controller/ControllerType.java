package controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ControllerType {
    STATIC_FILE_CONTROLLER("", StaticFileController.getInstance()),
    USER_CREATE_CONTROLLER("/users", UserCreateController.getInstance());
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
