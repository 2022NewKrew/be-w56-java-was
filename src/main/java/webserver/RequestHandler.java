package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import controller.Controller;
import controller.GetController;
import controller.PostController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.IOUtils;

import static util.HttpRequestUtils.*;

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

            // 요청 기본 정보 수집
            log.debug("REQ: {}", line);
            String[] tokens = line.split(" ");
            RequestStartLine requestStartLine = new RequestStartLine(new HashMap<>() {{
                put("Method", tokens[0]);
                put("Path", tokens[1]);
                put("Protocol", tokens[2]);
            }});

            // 요청 헤더 수집
            line = br.readLine();
            var tempRequestHeaders = new HashMap<String, String>();
            while (line != null && !line.equals("")) {
                String[] header = line.split(": ");
                Pair pair = parseHeader(line);
                tempRequestHeaders.put(pair.getKey(), pair.getValue());
                log.debug("     {}", line);

                line = br.readLine();
            }
            RequestHeaders requestHeaders = new RequestHeaders(tempRequestHeaders);

            // 요청 body 수집
            RequestBody requestBody = null;
            Set<String> headerKeys = requestHeaders.getHeaderKeys();
            if (headerKeys.contains("Content-Length")) {
                int contentLength = Integer.parseInt(requestHeaders.getHeader("Content-Length"));
                String content = IOUtils.readData(br, contentLength);
                requestBody = new RequestBody(content);
            }

            // 적합한 핸들러, 컨트롤러 메소드 찾기 -> 둘 중 하나라도 실패하면 처리 가능한 요청 아님
            String controllerMethodName = searchControllerMethod(requestStartLine);
            Controller controller = (controllerMethodName.equals("staticResource")) ? CONTROLLER_MAP.get("GET") : searchController(requestStartLine);

            // 응답 준비
            DataOutputStream dos = new DataOutputStream(out);

            // 응답 처리
            controller.doResponse(controllerMethodName, dos, requestStartLine, requestHeaders, requestBody);

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
