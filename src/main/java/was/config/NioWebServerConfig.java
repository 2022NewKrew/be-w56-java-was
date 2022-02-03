package was.config;


public interface NioWebServerConfig {
    default void port(NioWebServerConfigRegistry registry) {
    }

    default void workerEventLoopSize(NioWebServerConfigRegistry registry) {
    }
}
