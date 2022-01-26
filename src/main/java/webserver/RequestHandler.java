package webserver;

import controller.Controller;
import controller.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Request;
import util.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Controller controller;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controller = Controller.getInstance();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            Response response = new Response(out);

            Optional<Router> router = controller.getURL(request.getMethod(), request.getUrl());
            router.ifPresent((r) -> {
                try {
                    r.send(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (router.isEmpty()) {
                response.send(request.getUrl(), request.getMIME());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

