package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ControllerHandler;
import util.IOUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);
            Controller controller = ControllerHandler.getController(httpRequest.getUrlPath());
            HttpResponse httpResponse = controller.handle(httpRequest);

            httpResponse.flush(out);
            log.info("HTTP HttpRequest Header Lines : {}", httpRequest.getUrlPath());

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
