package webserver.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerMap {
    Map<String, Controller> controllerMap;

    public ControllerMap() {
        controllerMap = new HashMap<>();
        controllerMap.put("/", HomeController.getInstance());
        controllerMap.put("/user/create", UserController.getInstance());
        controllerMap.put("/user/login", UserController.getInstance());
        controllerMap.put("/user/list", UserController.getInstance());
    }

    public Controller getController(String path){
        if(Objects.isNull(path)){
            throw new IllegalArgumentException("path null");
        }
        return controllerMap.get(path);
    }
}
