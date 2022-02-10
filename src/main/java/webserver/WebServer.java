package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    public static void main(String[] args) throws Exception {

        try (ServerSocket serverSocket = new ServerSocket(WebServerConfig.PORT)) {
            log.info("Web Application Server started {} port.", WebServerConfig.PORT);

            Socket clientSocket;
            while ((clientSocket = serverSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                requestHandler.start();
            }
        }
    }
}
