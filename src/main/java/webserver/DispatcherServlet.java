package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.response.HttpResponse;
import webserver.controller.Controller;
import util.request.HttpRequest;
import util.request.HttpRequestReader;
import webserver.controller.ControllerMapping;
import webserver.view.ViewRenderer;

import java.io.*;
import java.net.Socket;

public class DispatcherServlet extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final ControllerMapping controllerMapping = new ControllerMapping();

    private final Socket connection;

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}"
                , connection.getInetAddress(), connection.getPort());

        try (HttpRequestReader hr = new HttpRequestReader(connection.getInputStream());
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            HttpRequest httpRequest = hr.read();
            Controller<?> controller = controllerMapping.getController(httpRequest).orElseThrow();

            HttpResponse<?> httpResponse = controller.handle(httpRequest);
            ViewRenderer.render(httpResponse, dos);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
