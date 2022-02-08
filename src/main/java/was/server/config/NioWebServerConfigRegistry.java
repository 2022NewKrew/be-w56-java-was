package was.server.config;

import java.util.HashMap;
import java.util.Map;

public class NioWebServerConfigRegistry {
    private int port = 8080;
    private int workerEventLoopSize = 1;
    private final Map<String, String> controllers = new HashMap<>();

    public void setPort(int port) {
        this.port = port;
    }

    public void setWorkerEventLoopSize(int workerEventLoopSize) {
        this.workerEventLoopSize = workerEventLoopSize;
    }

    public int getWorkerEventLoopSize() {
        return workerEventLoopSize;
    }

    public int getPort() {
        return port;
    }

    public void addController(String urlPath, String resourcePath) {
        controllers.put(urlPath, resourcePath);
    }

    public Map<String, String> getControllers() {
        return controllers;
    }
}
