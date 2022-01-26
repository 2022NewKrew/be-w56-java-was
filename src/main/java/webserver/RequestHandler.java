package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import util.request.HttpRequest;
import util.request.HttpRequestReader;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ControllerMapping controllerMapping = new ControllerMapping();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}"
                , connection.getInetAddress(), connection.getPort());

        try (HttpRequestReader hr = new HttpRequestReader(connection.getInputStream());
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            HttpRequest httpRequestMap = hr.read();
            getController(httpRequestMap).handle(httpRequestMap, dos);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private Controller getController(HttpRequest httpRequest){
        String url = httpRequest.getUrl();
        return controllerMapping.getController(url).orElseThrow();
    }
}
