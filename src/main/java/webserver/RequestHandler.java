package webserver;

import controller.Controller;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = HttpRequest.from(br);

            HandlerMapping handlerMapping = HandlerMapping.getInstance();
            Controller controller = handlerMapping.getController(httpRequest.getRequestLine().getPath().getPath());

            HttpResponse httpResponse = controller.service(httpRequest);
            IOUtils.write(new DataOutputStream(out), httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
