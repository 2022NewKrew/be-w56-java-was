package webserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.handler.HandlerFactory;
import webserver.request.Request;
import webserver.request.RequestReader;
import webserver.response.Response;
import webserver.response.ResponseFactory;
import webserver.response.ResponseWriter;

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
            e.printStackTrace();
        }

    }

    private void handleRequest(InputStream in, OutputStream out) {
        try {
            request = RequestReader.read(in);
            log.debug("request 생성");
            handler = HandlerFactory.createHandler(request);
            log.debug("handler 생성");
            response = handler.handle(request);
            log.debug("response 생성");
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
            e.printStackTrace();
            response = ResponseFactory.error(e);
        } finally {
            ResponseWriter.write(out, response);
        }
    }
}
