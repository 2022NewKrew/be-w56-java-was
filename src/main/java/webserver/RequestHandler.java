package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private RequestMapper requestMapper = new RequestMapper();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // http 요청과 응답을 만들어준다.
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            // url 에 맞는 컨트롤러를 찾아준다.
            String url = request.getUrl();
            if (url == null) return;
            Controller controller = requestMapper.mapping(url);

            // 컨트롤러가 응답을 처리한다.
            controller.makeResponse(request, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
