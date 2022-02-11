package webserver;

import java.io.*;
import java.net.Socket;

import model.MyHttpRequest;
import model.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            MyHttpRequest myHttpRequest = new MyHttpRequest(in);
            MyHttpResponse myHttpResponse = RequestMapper.createResponse(myHttpRequest, out);
            myHttpResponse.write();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
