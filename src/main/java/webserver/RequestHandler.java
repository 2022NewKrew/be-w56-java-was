package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ControllerMapping controllerMapping = new ControllerMapping();
    private static final RequestParser requestParser = new RequestParser();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}"
                , connection.getInetAddress(), connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            RequestMap requestMap = requestParser.parse(br);
            getController(requestMap).handle(requestMap, dos);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private Controller getController(RequestMap requestMap){
        String url = (String) requestMap.getHeader("url").orElseThrow();
        return controllerMapping.getController(url).orElseThrow();
    }
}
