package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;
import webserver.enums.MIME;
import webserver.model.ModelAndView;
import webserver.model.http.HttpRequest;
import webserver.model.http.HttpResponse;

public class DispatcherServlet extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private Socket connection;

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = HttpRequest.of(in);

            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setMime(MIME.parse(httpRequest.getUri()));

            ModelAndView modelAndView = HandlerAdapter.handle(httpRequest, httpResponse);
            ViewResolver.resolve(httpResponse, modelAndView);

            HttpResponseUtils.createResponse(out, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
