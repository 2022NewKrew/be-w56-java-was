package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends Thread {
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private Socket connection;

    public FrontController(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            Response response = new Response(out);
            HandlerMapper.map(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
