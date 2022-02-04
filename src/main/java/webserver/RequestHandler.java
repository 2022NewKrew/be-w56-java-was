package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import http.HttpBody;
import http.HttpHeader;
import http.HttpStatusCode;
import http.request.HttpRequest;
import http.request.HttpRequestStartLine;
import http.response.HttpResponse;
import http.util.HttpRequestUtils;
import http.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Router router = Router.getInstance();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequest.create(in);
            HttpResponse response = new HttpResponse(out);
            router.route(request, response);
            response.send();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
