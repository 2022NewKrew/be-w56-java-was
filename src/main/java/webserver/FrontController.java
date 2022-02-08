package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.view.ViewResolver;

public class FrontController extends Thread {
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private static final HandlerMapper handlerMapper = new HandlerMapper();
    private static final ViewResolver viewResolver = new ViewResolver();
    private final Socket connection;

    public FrontController(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            Response response = new Response(out);
            viewResolver.resolve(response, handlerMapper.map(request, response));
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }
}
