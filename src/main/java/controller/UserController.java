package controller;

import db.DataBase;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *  /user 로 시작하는 url request를 담당
 */
public class UserController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static UserController INSTANCE;
    private final Map<String, Function<HttpRequest, HttpResponse>> methodMap = new HashMap<>();

    {
        methodMap.put("POST /create", this::createUser);
        methodMap.put("POST /login", this::login);
    }

    private UserController() {
    }

    public static synchronized UserController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new UserController();
        return INSTANCE;
    }

    @Override
    public HttpResponse processDynamic(HttpRequest request) throws IOException {
        final HttpRequestLine requestLine = request.line();
        String[] urlTokens = requestLine.url().split("/");
        String methodAndUrl = requestLine.method() + " /";
        if (urlTokens.length > 0) {
            String urlWithoutQueryString = urlTokens[urlTokens.length - 1].split("\\?")[0];
            methodAndUrl += urlWithoutQueryString;
        }

        if (methodMap.containsKey(methodAndUrl)) {
            log.debug("{} called", methodAndUrl);
            return methodMap.get(methodAndUrl).apply(request);
        } else {
            log.debug("{} {}, redirect to error page", requestLine.method(), requestLine.url());
            return errorPage();
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

        if (DataBase.findUserById(newUser.getUserId()) == null)
            DataBase.addUser(newUser);

        return redirect("/index.html");
    }

    private HttpResponse login(HttpRequest request) {
        HttpRequestBody requestBody = request.body();
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody.content());

        User user = DataBase.findUserById(queryString.get("userId"));

        if (user == null || !user.getPassword().equals(queryString.get("password")))
            return redirect("/user/login_failed.html");

        HttpResponseHeader responseHeader = new HttpResponseHeader("index.html", HttpStatus.OK, 0);
        responseHeader.putToHeaders("Set-Cookie", "logined=true; Path=/");

        return new HttpResponse(responseHeader, HttpResponseBody.empty());
    }
}
