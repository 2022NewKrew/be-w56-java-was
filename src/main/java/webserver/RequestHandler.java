package webserver;

import java.io.*;
import java.net.Socket;

import model.MyHttpRequest;
import model.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

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
            MyHttpResponse myHttpResponse = new MyHttpResponse(myHttpRequest);
            HttpResponseUtils.response(out, myHttpResponse, myHttpRequest.getUri());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
