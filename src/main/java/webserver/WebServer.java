package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ArgsUtil;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        ArgsUtil argsUtil = ArgsUtil.from(args);
        try (ServerSocket listenSocket = new ServerSocket(DEFAULT_PORT)) {
            log.info("Web Application Server started {} port.", DEFAULT_PORT);
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executorService.execute(new RequestHandler(connection));
            }
        }
    }
}
