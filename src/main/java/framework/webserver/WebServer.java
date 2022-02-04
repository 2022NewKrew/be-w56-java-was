package framework.webserver;

import framework.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void run(Class<?> baseClass, String[] args) {
        Container.scanAndFill(baseClass);

        int port = 0;

        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            LOGGER.info("Web Application Server started {} port.", port);

            Socket connection;

            while ((connection = listenSocket.accept()) != null) {
                ConnectionThread connectionHandler = new ConnectionThread(connection);
                connectionHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
