package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.exception.ExceptionResolver;
import webserver.processor.handler.controller.Controller;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private final ExecutorService executorService;
    private final int listenPort;

    WebServer(List<Controller> controllers, List<ExceptionResolver> exceptionResolvers, int threadPoolSize, int port) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.listenPort = port;
        HttpFactory.initialize(controllers, exceptionResolvers);
    }

    public void start() throws Exception {
        try (ServerSocket listenSocket = new ServerSocket(listenPort)) {
            log.info("Web Application Server started {} port.", listenPort);
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executorService.execute(new RequestHandler(connection));
            }
        }
    }
}
