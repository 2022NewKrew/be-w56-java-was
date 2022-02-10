package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.router.MainRouter;


@Slf4j
public class RequestHandler extends Thread {
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

//            MainRouter.routing(httpRequest, httpResponse);
            MainRouter.route(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
