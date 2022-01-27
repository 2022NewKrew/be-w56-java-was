package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import controller.LoginController;
import controller.ResoureController;
import controller.SignUpController;
import http.HttpHeaders;
import http.request.HttpRequest;
import http.request.parser.HttpRequestParser;
import http.response.HttpResponse;
import http.response.HttpResponseSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpRequestParser httpRequestParser = new HttpRequestParser();
    public List<Controller> controllers = new ArrayList<>();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllers.add(new SignUpController());
        controllers.add(new LoginController());
        controllers.add(new ResoureController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = httpRequestParser.parse(in);
            loggingRequestHeader(httpRequest);
            HttpResponse httpResponse = new HttpResponse();

            Controller controller = findController(httpRequest);
            controller.handle(httpRequest, httpResponse);

            HttpResponseSender httpResponseSender = new HttpResponseSender(httpResponse);
            httpResponseSender.send(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loggingRequestHeader(HttpRequest httpRequest) {
        for (Map.Entry header : httpRequest.getHeaders().entrySet()) {
            log.info("%s: %s", header.getKey(), header.getValue());
        }
    }

    private Controller findController(HttpRequest httpRequest) {
        return controllers.stream()
                .filter(controller -> controller.isSupported(httpRequest))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할수 없습니다."));
    }

}
