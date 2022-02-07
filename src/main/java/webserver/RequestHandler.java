package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.*;
import http.HttpHeaders;
import http.request.HttpRequest;
import http.request.parser.HttpRequestParser;
import http.response.HttpResponse;
import http.response.HttpResponseSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import renderer.UserListPageRenderer;

public class RequestHandler extends Thread {

    private final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpRequestParser httpRequestParser = new HttpRequestParser();
    public List<Controller> controllers = new ArrayList<>();

    private Socket connection;

    /**
     * ResourceController는 마지막에 등록한다.
     * @param connectionSocket
     */
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllers.add(new SignUpController());
        controllers.add(new LoginController());
        controllers.add(new UserListController(new UserListPageRenderer()));
        controllers.add(new ResoureController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
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
        for (Map.Entry header : httpRequest.getRequestHeaders().getHeaders().entrySet()) {
            log.info("{} {}", header.getKey(), header.getValue());
        }
    }

    private Controller findController(HttpRequest httpRequest) {
        return controllers.stream()
                .filter(controller -> controller.isSupported(httpRequest))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할수 없습니다."));
    }

}
