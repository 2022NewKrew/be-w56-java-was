package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.MyHttpRequest;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
//        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {

            MyHttpRequest myHttpRequest = new MyHttpRequest(br);
            log.info(myHttpRequest.toString());

            Controller.mapping(myHttpRequest, new DataOutputStream(out));

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
