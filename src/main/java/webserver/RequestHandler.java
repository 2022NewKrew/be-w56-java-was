package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import collections.RequestStartLine;
import controller.Controller;
import controller.GetController;
import controller.PostController;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final UserService USER_SERVICE = new UserService();

    private static final GetController GET_CONTROLLER = new GetController(USER_SERVICE);
    private static final PostController POST_CONTROLLER = new PostController(USER_SERVICE);

    private static final Map<String, Controller> CONTROLLER_MAP = new HashMap<>(){{
        put("GET", GET_CONTROLLER);
        put("POST", POST_CONTROLLER);
    }};
    private static final Map<String, String> PATH_METHOD_MAP = new HashMap<>() {{
        put("/user/create", "userCreate");
        put("/user/login", "userLogin");
        put("/user/list", "userList");
        put("/", "index");
        put("/user/logout", "userLogout");
        put("/post/create", "post");
    }};

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 요청 확인
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            HttpRequest httpRequest = HttpRequest.of(br, line);

            // 적합한 핸들러, 컨트롤러 메소드 찾기 -> 둘 중 하나라도 실패하면 처리 가능한 요청 아님
            String controllerMethodName = searchControllerMethod(httpRequest.getRequestStartLine());
            Controller controller = (controllerMethodName.equals("staticResource")) ? CONTROLLER_MAP.get("GET") : searchController(httpRequest.getRequestStartLine());

            // 응답 준비
            DataOutputStream dos = new DataOutputStream(out);

            // 응답 처리
            controller.doResponse(controllerMethodName, dos, httpRequest);

        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("존재하지 않는 페이지입니다.");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            log.error("요청이 잘못되었습니다.");
        }
    }

    private String searchControllerMethod(RequestStartLine requestStartLine) {
        return PATH_METHOD_MAP.getOrDefault(requestStartLine.getPath(), "staticResource");
    }

    private Controller searchController(RequestStartLine requestStartLine) {
        return CONTROLLER_MAP.get(requestStartLine.getMethod());
    }

}
