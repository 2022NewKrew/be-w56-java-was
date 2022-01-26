package controller;

import db.DataBase;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *  싱글톤
 *
 *  process 메소드를 통해 HttpRequest 를 넘겨주면
 *  필요한 작업 후 HttpResponse 를 반환
 */
public class UserController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static UserController INSTANCE;

    private UserController() {
    }

    public static synchronized UserController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new UserController();
        return INSTANCE;
    }

    public HttpResponse process(HttpRequest request) throws IOException {
        final HttpRequestLine requestLine = request.line();
        String[] urlTokens = requestLine.url().split("/");
        String url = urlTokens[urlTokens.length - 1].split("\\?")[0];

        if (requestLine.method().equals("GET") && url.equals("create")) {
            log.debug("GET /create called");
            return createUser(requestLine);
        } else { // TODO : 임시 구현
            log.debug("{} {}", requestLine.method(), requestLine.url());
            return readStaticFile(requestLine.url());
        }
    }

    private HttpResponse createUser(HttpRequestLine requestLine) {
        Map<String, String> queryString = requestLine.queryString();
        System.out.println(queryString);
        User newUser = new User(
                queryString.get("userId"),
                queryString.get("password"),
                queryString.get("name"),
                queryString.get("email"));

        DataBase.addUser(newUser);
        return redirect("/index.html");
    }
}
