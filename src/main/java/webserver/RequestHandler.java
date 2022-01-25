package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Controller controller = new Controller();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            ParsedRequest parsedRequest = new ParsedRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            controller.makeResponse(parsedRequest, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
