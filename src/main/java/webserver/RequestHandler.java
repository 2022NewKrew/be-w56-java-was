package webserver;

import controller.Controller;
import controller.ControllerType;
import exceptions.InvalidHttpMethodException;
import exceptions.exceptionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    private void runHttp(InputStream in, OutputStream out) throws IOException{
        try {
            HttpRequest httpRequest = HttpRequest.of(in);
            Controller controller = ControllerType.getControllerType(httpRequest.getUrl());

            HttpResponse httpResponse = controller.run(httpRequest);
            View.sendResponse(out, httpResponse.message());
        } catch (InvalidHttpMethodException invalidHttpMethodException) {
            exceptionHandler.httpMethodNotFound(out, invalidHttpMethodException.getMessage());
        }
    }

    public void run() {
        log.debug("요청: IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();) {
            runHttp(in, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
