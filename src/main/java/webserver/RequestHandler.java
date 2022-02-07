package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import controller.ControllerManager;
import http.Request;
import http.Response;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            Request request = new Request(in);
            String path = ControllerManager.matchController(request);
            Response response = new Response(request, path, out);
            response.send();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
