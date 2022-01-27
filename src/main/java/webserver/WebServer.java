package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.custom.ComponentManager;
import webserver.custom.DispatcherServlet;
import webserver.custom.HandlerMapping;

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

        // Custom DispatcherServlet set-up
        Dispatcher customDispatcherServlet = new DispatcherServlet(HandlerMapping.of(ComponentManager.of()));

        // ���� dispatcher
        Dispatcher defaultDispatcherServlet = new DefaultDispatcher();

        // ���������� �����Ѵ�. �������� �⺻������ 8080�� ��Ʈ�� ����Ѵ�.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // Ŭ���̾�Ʈ�� ����ɶ����� ����Ѵ�.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, defaultDispatcherServlet);
                requestHandler.start();
            }
        }
    }
}
