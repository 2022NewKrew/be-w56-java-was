package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.request.HttpRequestReader;
import util.response.HttpResponse;
import webserver.controller.Controller;
import webserver.controller.ControllerMapping;
import webserver.view.ViewRenderer;

import java.io.DataOutputStream;
import java.net.Socket;

public class DispatcherServlet extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ControllerMapping controllerMapping = new ControllerMapping();
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
            Controller<?> controller = controllerMapping.getController(httpRequest);

            HttpResponse<?> httpResponse = controller.handle(httpRequest);
            ViewRenderer.render(httpResponse, dos);
        }catch (Exception exception){
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
