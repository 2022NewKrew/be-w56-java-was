package framework.webserver;

import java.io.*;
import java.net.Socket;
import framework.Beans;
import framework.FrontController;
import org.slf4j.Logger;

public class RequestHandler extends Thread {
    private final Logger log = Beans.log;
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            FrontController frontController = Beans.frontController;
            DataOutputStream dos = frontController.request(in, out);
            dos.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
