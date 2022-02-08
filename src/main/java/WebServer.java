import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.container.ServletContainer;
import webserver.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    private static final ServletContainer servletContainer = ServletContainer.getInstance();

    public static void main(String[] args) throws Exception {
        int port = assignPort(args);
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);
            waitConnection(listenSocket);
        }
    }

    private static int assignPort(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(args[0]);
    }

    private static void waitConnection(ServerSocket listenSocket) throws IOException {
        // TODO thread pool
        Socket connection;
        while((connection = listenSocket.accept()) != null) {
            ConnectionHandler connectionHandler = new ConnectionHandler(connection, servletContainer);
            connectionHandler.start();
        }
    }
}
