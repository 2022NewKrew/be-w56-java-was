package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServerSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(WebServerSocketHandler.class);
    private final int port;
    private final ExecutorService executorService;
    private final RequestHandler requestHandler;

    public WebServerSocketHandler(int port){
        this.port = port;
        executorService = Executors.newCachedThreadPool();
        requestHandler = new RequestHandler();
    }

    public void run(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            log.info("Web Application Server started {} port.", port);

            Socket socket;
            while((socket = serverSocket.accept()) != null){
                Socket finalSocket = socket;
                executorService.execute(() -> requestHandler.sendResponseMessage(finalSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
