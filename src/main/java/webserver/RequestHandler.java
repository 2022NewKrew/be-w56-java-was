package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ViewResolver viewResolver;

    public RequestHandler(Socket connectionSocket) {
        this(connectionSocket, new ViewResolver());
    }

    public RequestHandler(Socket connection, ViewResolver viewResolver) {
        this.connection = connection;
        this.viewResolver = viewResolver;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);

            viewResolver.render(out, request.getUrl());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
