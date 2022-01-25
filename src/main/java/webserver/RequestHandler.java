package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connected! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);
            log.info(httpRequest.toString());
            HttpResponse httpResponse = handleRequest(httpRequest);
            log.info(httpResponse.toString());
            httpResponse.send(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse handleRequest(HttpRequest request) throws IOException {
        return HttpResponse.of(request, request.getHttpUri());
    }
}
