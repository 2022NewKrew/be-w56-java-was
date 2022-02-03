package application.config;

import was.config.NioWebServerConfig;
import was.config.NioWebServerConfigRegistry;

public class WebConfig implements NioWebServerConfig {

    @Override
    public void workerEventLoopSize(NioWebServerConfigRegistry registry) {
        registry.setWorkerEventLoopSize(16);
    }
}
