package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import controller.Controller;
import controller.ResoureController;
import controller.SignUpController;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public List<Controller> controllers = new ArrayList<>();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllers.add(new SignUpController());
        controllers.add(new ResoureController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(in);
            httpRequest.loggingRequestHeader();
            HttpResponse httpResponse = new HttpResponse(new DataOutputStream(out));

            Controller controller = findController(httpRequest);
            controller.handle(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Controller findController(HttpRequest httpRequest) {
        return controllers.stream()
                .filter(controller -> controller.isSupported(httpRequest))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할수 없습니다."));
    }

}
