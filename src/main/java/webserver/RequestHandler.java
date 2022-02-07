package webserver;

import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            Controller controller = RequestMapping.getController(request.getRequestLine().getUrl());
            if (controller == null) {
                response.forward(request.getRequestLine().getUrl());
                return;
            }
            controller.service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
