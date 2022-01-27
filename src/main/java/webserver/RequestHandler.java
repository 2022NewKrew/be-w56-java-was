package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.controller.Controller;
import webserver.controller.DispatcherServlet;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;
import webserver.view.View;

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
            Method method = DispatcherServlet.handlerMapping(httpRequest);
            HttpResponse httpResponse = (HttpResponse) method.invoke(new Controller(), httpRequest);
            View.render(out, httpResponse);

        } catch (IOException | URISyntaxException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
    }


}
