package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.DefaultHttpRequestBuilder;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.DispatcherServlet;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final DispatcherServlet dispatcherServlet = DispatcherServlet.getInstance();
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new DefaultHttpRequestBuilder(reader).build();
            HttpResponse response = new HttpResponse(out);

            dispatcherServlet.handle(request, response);
            response.send();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
