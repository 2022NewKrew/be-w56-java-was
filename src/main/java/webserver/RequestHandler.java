package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.infra.Router;
import webserver.infra.ViewResolver;
import webserver.model.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);

            // Mapping to Controller by HttpRequest (HttpMethod & URL)
            String viewPath = router.route(request);

            viewResolver.render(out, viewPath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
