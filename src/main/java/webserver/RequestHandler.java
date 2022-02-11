package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RequestMapper mapper;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.mapper = RequestMapper.getInstance();
        log.debug("Request Handler 생성! Connection : {}, mapper : {} ", connection, mapper);
     }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest req = new HttpRequest(in);
            HttpResponse res = handle(req, out);
            res.write();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse handle(HttpRequest req, OutputStream out) throws IOException {
        if (mapper.getController(req).isEmpty()) {
            log.trace("handle is empty");
            return new HttpResponse.Builder(out)
                    .setHttpStatus(HttpStatus._404)
                    .build();
        }
        log.debug("exact handle {} ", mapper.getController(req));
        return mapper.getController(req).get().handleRequest(req, out);
    }
}


