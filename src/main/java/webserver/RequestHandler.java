package webserver;

import http.request.HttpRequest;
import http.request.HttpRequestDecoder;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import webserver.controller.AbstractController;
import webserver.controller.SignupController;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestHandler extends Thread {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    private static final Map<String, AbstractController> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new SignupController());
//        controllerMap.put("/user/login", new LoginController());
//        controllerMap.put("/user/list", new UserListController());
    }


    // TODO: thread -> event loop
    // TODO: keep-alive
    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             DataOutputStream dos = new DataOutputStream(out);
        ) {
            HttpRequest request = HttpRequestDecoder.decode(br);
            AbstractController controller = getController(request.getUri());

            HttpResponse response = controller.service(request);
            response.send(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /* ---------------------------------------------------------------------- */

    public static AbstractController getController(String path) {
        return controllerMap
                .getOrDefault(path, new AbstractController());
    }
}
