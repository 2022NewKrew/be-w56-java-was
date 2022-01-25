package webserver.config;

import webserver.controller.HttpController;
import webserver.controller.RegisterController;

import java.util.HashMap;
import java.util.Map;

public class ControllerConfig {
    public static final Map<String, HttpController> controllerMap = new HashMap<>(){{
        put("/user/create", new RegisterController());
    }};
}
