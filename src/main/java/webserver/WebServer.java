package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port;
        port = transformPort(args);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start();
            }
        }
    }

    private static int transformPort(String[] args) {
        int port;
        try {
            port = Integer.parseInt(args[0]);
            if (port < MIN_PORT || port > MAX_PORT) {
                port = DEFAULT_PORT;
            }
            return port;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return DEFAULT_PORT;
        }
    }
}
