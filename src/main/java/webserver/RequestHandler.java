package webserver;

import controller.BaseController;
import controller.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final BaseController controller;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controller = BaseController.getInstance();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out, request);

            Optional<Router> router = controller.getURL(request.getMethod(), request.getUrl());
            router.ifPresent((r) -> {
                try {
                    r.send(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (router.isEmpty()) {
                response.initBody(request.getUrl());
                response.send(request.getUrl());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

