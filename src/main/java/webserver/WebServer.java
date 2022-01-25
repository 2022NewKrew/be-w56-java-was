package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            Socket clientSocket;
            while ((clientSocket = serverSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                requestHandler.start();
            }
        }
    }
}
