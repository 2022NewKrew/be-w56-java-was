package com.kakao.webserver;

import com.kakao.http.request.HttpRequest;
import com.kakao.util.ReflectionUtils;
import com.kakao.webserver.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final List<Controller> controllerList;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerList = ReflectionUtils.getInstancesImplementedInterface(Controller.class);
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            handleRequest(httpRequest, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handleRequest(HttpRequest httpRequest, OutputStream out) {
        for (Controller controller : controllerList) {
            if (controller.isValidPath(httpRequest.getUrl().getPath())) {
                controller.handleRequest(httpRequest, out);
                return;
            }
        }
    }
}
