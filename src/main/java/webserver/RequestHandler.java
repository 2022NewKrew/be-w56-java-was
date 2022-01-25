package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.InvalidInputException;
import webserver.infra.Router;
import webserver.infra.ViewResolver;
import webserver.model.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Router router = Router.getInstance();
    private final ViewResolver viewResolver = ViewResolver.getInstance();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        ) {
            handleRequest(br, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handleRequest(BufferedReader br, DataOutputStream dos) throws IOException {
        HttpRequest request = new HttpRequest(br);

        try {
            String viewPath = router.route(request);
            viewResolver.render(dos, viewPath);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            viewResolver.renderBadRequest(dos);
        }
    }
}
