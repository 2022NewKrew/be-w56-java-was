package webserver;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchProviderException;

import webserver.controller.Controller;
import webserver.controller.ControllerCommander;
import webserver.http.domain.Cookie;
import webserver.http.domain.CookieConst;
import webserver.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequestParser;
import webserver.http.response.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest httpRequest = HttpRequestParser.parse(in);
            HttpResponse httpResponse = new HttpResponse(dos);

            Cookie cookie = httpRequest.getCookie();
            System.out.println(cookie.getCookie(CookieConst.LOGIN_COOKIE.toString()));

            handleRequest(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
        log.info("[REQUEST URI] - " + request.getRequestUri());
        try {
            Controller controller = ControllerCommander.findController(request);
            if(controller == null) {
                response.setUrl(request.getRequestUri());
                response.forward();
                return;
            }

            controller.execute(request, response);

        } catch (NoSuchProviderException e) {
            response.setStatusCode(404, "Not Found");
            System.out.println("handle Error");
            e.printStackTrace();
        }
    }
}
