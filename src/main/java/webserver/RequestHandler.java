package webserver;

import controller.Controller;
import controller.ControllerType;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("요청: IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();) {
            HttpRequest httpRequest = HttpRequest.of(in);
            Controller controller = ControllerType.getControllerType(httpRequest.getUrl());

            HttpResponse httpResponse = controller.run(httpRequest);
            httpResponse.sendResponse(connection);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
