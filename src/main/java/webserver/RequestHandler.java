package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.GetController;
import webserver.controller.MethodController;
import webserver.controller.PostController;
import webserver.manage.RequestParser;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            RequestParser rp = new RequestParser(in);

            requestMethodMapping(rp, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void requestMethodMapping (RequestParser rp, OutputStream os) throws IOException {
        MethodController methodController = null;

        switch (rp.getMethod()) {
            case "GET" :
                methodController = new GetController(rp, os);
                break;
            case "POST":
                methodController = new PostController(rp, os);
                break;
            case "PUT":
                break;
            case "DELETE":
                break;
        }
        methodController.service();
    }
}
