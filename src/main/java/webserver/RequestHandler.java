package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.web.request.Request;
import webserver.web.response.Response;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final DispatcherServlet dispatcherServlet;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.dispatcherServlet = DispatcherServlet.getInstance();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = Request.getRequest(in);
            Response response = dispatcherServlet.serve(request);
            response.send(out);
        } catch (Exception e) {
            log.error("{} : error in Request Handler", e.getMessage());
        }
    }
}
