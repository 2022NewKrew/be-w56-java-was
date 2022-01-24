package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);

    private final Socket connection;

    public ConnectionHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        LOGGER.debug("Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestHandler requestHandler = new RequestHandler(in);
            ResponseHandler responseHandler = new ResponseHandler(requestHandler);
            responseHandler.makeHeaderAndFlush(out);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
