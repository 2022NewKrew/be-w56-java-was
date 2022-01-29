package was.config;


public interface NioWebServerConfig {
    void registerController(NioWebServerConfigRegistry registry);

    default void port(NioWebServerConfigRegistry registry) {
    }

    default void workerEventLoopSize(NioWebServerConfigRegistry registry) {
    }
}
