package webserver;

import exception.CustomException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.handler.HandlerFactory;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.StatusCode;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private Request request;
    private Response response;
    private Handler handler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            handleRequest(in, out);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private void handleRequest(InputStream in, OutputStream out) {
        try {
            request = Request.create(in);
            response = Response.create(out);

            handler = HandlerFactory.createHandler(request);
            handler.handle(request, response);
        } catch (CustomException e) {
            log.error("CustomException occurred: {}", e.getMessage());
            response = Response.createErrorResponse(out, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
            response = Response.createErrorResponse(out, StatusCode.INTERNAL_SERVER_ERROR,
                e.getMessage());
        } finally {
            log.debug("finally: {}", connection.isClosed());
            response.write();
        }
    }
}
