package controller;

import db.DataBase;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *  싱글톤
 *
 *  process 메소드를 통해 HttpRequest 를 넘겨주면
 *  필요한 작업 후 HttpResponse 를 반환
 */
public class UserController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static UserController INSTANCE;
    private final Map<String, Function<HttpRequest, HttpResponse>> methodMap = new HashMap<>();

    {
        methodMap.put("POST /create", this::createUser);
    }

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
        String urlWithoutQueryString = urlTokens[urlTokens.length - 1].split("\\?")[0];
        String methodAndUrl = requestLine.method() + " /" + urlWithoutQueryString;

        if (methodMap.containsKey(methodAndUrl)) {
            log.debug("{} called", methodAndUrl);
            return methodMap.get(methodAndUrl).apply(request);
        } else { // TODO : 임시 구현
            log.debug("{} {}", requestLine.method(), requestLine.url());
            return readStaticFile(requestLine.url());
        }
    }

    private HttpResponse createUser(HttpRequest request) {
        HttpRequestBody requestBody = request.body();
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody.content());
        User newUser = new User(
                queryString.get("userId"),
                queryString.get("password"),
                queryString.get("name"),
                queryString.get("email"));

        DataBase.addUser(newUser);
        return redirect("/index.html");
    }
}
