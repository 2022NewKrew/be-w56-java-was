package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            Socket connection;
            ExecutorService service = Executors.newCachedThreadPool();

            while ((connection = listenSocket.accept()) != null) {
                Socket finalConnection = connection;
                service.execute(()->{
                    RequestHandler requestHandler = new RequestHandler(finalConnection);
                    requestHandler.start();
                });
            }
        }
    }
}
