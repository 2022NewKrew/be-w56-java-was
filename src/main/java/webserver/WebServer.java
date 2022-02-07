package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.core.*;
import webserver.handler.ArgumentResolverHandler;
import webserver.handler.InterceptorHandler;
import webserver.handler.WebConfigHandler;

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

        // Custom Spring DispatcherServlet set-up
        WebConfigHandler.init(ComponentManager.getInstance(), ArgumentResolverHandler.getInstance(), InterceptorHandler.getInstance());
        Dispatcher customDispatcherServlet = new DispatcherServlet(InterceptorHandler.getInstance(), HandlerMapping.getInstance(ComponentManager.getInstance()));

        // ���� dispatcher
        Dispatcher defaultDispatcherServlet = new DefaultDispatcher();

        // ���������� �����Ѵ�. �������� �⺻������ 8080�� ��Ʈ�� ����Ѵ�.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // Ŭ���̾�Ʈ�� ����ɶ����� ����Ѵ�.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, customDispatcherServlet);
                requestHandler.start();
            }
        }
    }
}
