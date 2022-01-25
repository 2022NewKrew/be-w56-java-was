package webserver;

import java.io.*;
import java.net.Socket;

<<<<<<< HEAD
import framework.Beans;
import framework.FrontController;
import org.slf4j.Logger;

public class RequestHandler extends Thread {
    private final Logger log = Beans.log;
=======
import controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
>>>>>>> 93a3f59 (프로젝트 구조 spring mvc와 유사하게 변경)
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
<<<<<<< HEAD
            FrontController frontController = Beans.frontController;
            DataOutputStream dos = frontController.request(in, out);
            dos.close();
=======
            FrontController frontController = new FrontController();
            DataOutputStream dos = frontController.request(in, out);
            dos.flush();
>>>>>>> 93a3f59 (프로젝트 구조 spring mvc와 유사하게 변경)
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
