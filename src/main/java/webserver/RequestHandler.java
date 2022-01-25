package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.SocketErrorException;
import webserver.processor.HttpProcessor;

public class RequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestHandlerInternal requestHandlerInternal = new RequestHandlerInternal();
            requestHandlerInternal.run(in, out);
        } catch (IOException e) {
            throw new SocketErrorException("Already Socket Closed or Socket Connection Refused", e);
        }
    }
}
