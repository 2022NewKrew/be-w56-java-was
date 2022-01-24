package webserver;

import java.io.*;
import java.net.Socket;

import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpResponse;
import http.HttpResponseRenderer;
import http.impl.HttpFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.SocketErrorException;

public class RequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final HttpProcessor httpProcessor;
    private final Socket connection;

    public RequestHandler(Socket connectionSocket, HttpProcessor httpProcessor) {
        this.httpProcessor = httpProcessor;
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestHandlerInternal requestHandlerInternal = new RequestHandlerInternal();
            requestHandlerInternal.run(in, out, httpProcessor);
        } catch (IOException e) {
            throw new SocketErrorException("Already Socket Closed or Socket Connection Refused", e);
        }
    }
}
