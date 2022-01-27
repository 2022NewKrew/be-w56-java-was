package infrastructure.config;

public class ServerConfig {

    public static final String SERVER_DOMAIN = "http://localhost";
    public static final int DEFAULT_PORT = 8081;
    public static final String DEFAULT_RESOURCE_PATH = "./webapp";
    public static final String SUPPORTEDVERSION = "HTTP/1.1";
    public static final String COLON = ":";
    public static final String DELIMITER = " ";

    private ServerConfig() {
    }

    public static String getAuthority() {
        return SERVER_DOMAIN + COLON + DEFAULT_PORT;
    }
}
