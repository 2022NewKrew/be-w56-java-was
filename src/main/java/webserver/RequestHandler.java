package webserver;

import http.request.HttpRequest;
import http.request.HttpRequestDecoder;
import http.response.HttpResponse;
import http.view.OutputView;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.controller.BaseController;
import webserver.controller.LoginController;
import webserver.controller.SignupController;
import webserver.controller.UserListController;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class RequestHandler implements Runnable {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    private static final Map<String, BaseController> controllerMap = new HashMap<>();
    private static final Map<Long, User> sessionMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new SignupController());
        controllerMap.put("/user/login", new LoginController());
        controllerMap.put("/user/list", new UserListController());
    }

    // TODO: thread -> event loop
    // TODO: keep-alive
    @Override
    public void run() {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             DataOutputStream dos = new DataOutputStream(out);
        ) {
            HttpRequest request = HttpRequestDecoder.decode(br);
            BaseController controller = getController(request.getUri());

            HttpResponse response = controller.service(request);
            OutputView.sendResponse(response, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /* ---------------------------------------------------------------------- */

    public static BaseController getController(String path) {
        return controllerMap
                .getOrDefault(path, new BaseController());
    }

    public static Long addSessionUser(User user) {
        Long sessionId;
        Random random = new Random();

        do {
            sessionId = random.nextLong();
        } while (sessionMap.containsKey(sessionId));

        sessionMap.put(sessionId, user);
        return sessionId;
    }

    public static User getSessionUser(Long sessionId) {
        return sessionMap.get(sessionId);
    }
}
