package webserver;

import http.HttpRequest;
import http.HttpResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.parse(in);

            String view = MappingHandler.invoke(httpRequest);
            HttpResponse httpResponse = httpRequest.respond();
            httpResponse.changeView(view);

            ViewHandler.handle(out, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
