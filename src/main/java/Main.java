import was.Server;
import was.ServerConfig;

public class Main {

    public static void main(String[] args) {
        final ServerConfig serverConfig = new ServerConfig();
        final Server server = new Server(serverConfig);
        server.run();
    }
}
