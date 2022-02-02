package infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastructure.dispatcher.DispatcherServlet;
import http.request.HttpRequest;
import http.response.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            HttpRequest httpRequest = HttpRequest.from(in);
            HttpResponse httpResponse = DispatcherServlet.handle(httpRequest);
            httpResponse.write(out);

        } catch (IOException | URISyntaxException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            log.error(e.getMessage());
        }
    }


}
