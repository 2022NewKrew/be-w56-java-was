package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import handler.HandlerMapping;
import handler.HandlerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.request.HttpRequest;
import http.response.HttpResponse;

public class RequestDispatcher extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestDispatcher.class);

    private final Socket connection;

    public RequestDispatcher(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connected! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);
            log.info(httpRequest.toString());
            HandlerMethod handlerMethod = HandlerMapping.findHandlerMethodOf(httpRequest);
            if (handlerMethod == null) {
                HttpResponse httpResponse = handleRequest(httpRequest);
                httpResponse.send(out);
                return;
            }
            HttpResponse httpResponse;
            try {
                String viewName = handlerMethod.invoke(httpRequest);
                httpResponse = HttpResponse.of(httpRequest, viewName);
            } catch (Exception e) {
                log.error(e.getMessage());
                httpResponse = HttpResponse.error();
            }
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
