package was.config;

import was.domain.controller.Controller;
import was.domain.http.MethodAndPath;
import was.meta.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class NioWebServerConfigRegistry {
    private final Map<MethodAndPath, Controller> controllers = new HashMap<>();
    private int port = 8080;

    public void setPort(int port) {
        this.port = port;
    }

    public void addController(HttpMethod method, String path, Controller controller) {
        final MethodAndPath methodAndPath = new MethodAndPath(method.name(), path);
        controllers.put(methodAndPath, controller);
    }

    public Map<MethodAndPath, Controller> getControllers() {
        return controllers;
    }

    public int getPort() {
        return port;
    }
}
