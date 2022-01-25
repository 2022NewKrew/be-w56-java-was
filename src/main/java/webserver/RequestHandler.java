package webserver;

import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUtills;
import util.ResponseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            Request request = RequestUtills.generateRequest(in);
            ResponseUtils.response(out, request);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
