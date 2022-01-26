import was.NioWebServer;
import was.NioWebServerConfig;

public class Main {
    public static void main(String[] args) {
        final NioWebServerConfig nioWebServerConfig = new NioWebServerConfig();

        final NioWebServer nioWebServer = new NioWebServer(nioWebServerConfig);
        nioWebServer.run();
    }
}
