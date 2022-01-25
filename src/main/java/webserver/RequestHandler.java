package webserver;

import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.List;
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

            List<String> requestLines = IOUtils.getRequestLines(br);
            HttpRequest httpRequest = HttpRequest.from(requestLines);
            String body = IOUtils.readData(br, httpRequest.getContentLength());
            httpRequest.setParams(body);

            HandlerMapping handlerMapping = HandlerMapping.getInstance();
            Controller controller = handlerMapping.getController(httpRequest.getPath());

            HttpResponse httpResponse = controller.service(httpRequest);
            IOUtils.write(new DataOutputStream(out), httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
