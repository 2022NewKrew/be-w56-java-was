package was.server.config;


import was.http.domain.request.MethodAndPath;

public interface NioWebServerConfig {
    default void port(NioWebServerConfigRegistry registry) {
    }

    default void workerEventLoopSize(NioWebServerConfigRegistry registry) {
    }

    default void addController(NioWebServerConfigRegistry registry) {
    }
}
