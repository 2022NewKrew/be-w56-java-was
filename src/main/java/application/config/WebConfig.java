package application.config;

import was.server.config.NioWebServerConfig;
import was.server.config.NioWebServerConfigRegistry;

public class WebConfig implements NioWebServerConfig {

    @Override
    public void workerEventLoopSize(NioWebServerConfigRegistry registry) {
        registry.setWorkerEventLoopSize(16);
    }

    @Override
    public void addController(NioWebServerConfigRegistry registry) {
        registry.addController("/favicon.ico", "/favicon.ico");
    }
}
