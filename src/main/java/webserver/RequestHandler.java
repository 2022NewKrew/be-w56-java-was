package webserver;

import java.io.*;
import java.net.Socket;

import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpRequest;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequest.from(in);
            if (request.getMethod().equals("POST")) {
                System.out.println(request.getHeaders());
                System.out.println(request.getBody());
            }
            RequestMapper mapper = new RequestMapper(request);
            HttpResponse response = mapper.getResponse();
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
