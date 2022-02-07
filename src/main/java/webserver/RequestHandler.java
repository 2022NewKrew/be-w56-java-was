package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Controller.Controller;
import webserver.Controller.UserController;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Controller controller = new Controller();
    private static final UserController userController = new UserController();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            RequestHeader header = new RequestHeader(br);

            switch (header.getUri()) {
                case "/user/create":
                    userController.signUp(dos, header);
                    break;
                case "/user/login":
                    userController.login(dos, header);
                    break;
                case "/user/list":
                    userController.userList(dos, header);
                    break;
                default:
                    controller.responseStaticFile(dos, header);
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
