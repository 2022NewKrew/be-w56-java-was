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
import model.UserDBConstants;
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
        methodMap.put("POST /user/create", this::createUser);
        methodMap.put("POST /user/login", this::login);
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

        if (methodMap.containsKey(requestLine.methodAndPath())) {
            log.debug("{} called", requestLine.methodAndPath());
            return methodMap.get(requestLine.methodAndPath()).apply(request);
        } else {
            log.debug("{} {} redirect to error page", requestLine.method(), requestLine.path());
            return errorPage();
        }
    }

    private HttpResponse createUser(HttpRequest request) {
        HttpRequestBody requestBody = request.body();
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody.content());

        User newUser = new User(
                queryString.get(UserDBConstants.COLUMN_USER_ID),
                queryString.get(UserDBConstants.COLUMN_PASSWORD),
                queryString.get(UserDBConstants.COLUMN_NAME),
                queryString.get(UserDBConstants.COLUMN_EMAIL));

        if (DataBase.findUserById(newUser.getUserId()) == null)
            DataBase.addUser(newUser);

        return redirect("/index.html");
    }

    private HttpResponse login(HttpRequest request) {
        HttpRequestBody requestBody = request.body();
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody.content());

        User user = DataBase.findUserById(queryString.get(UserDBConstants.COLUMN_USER_ID));

        if (user == null || !user.getPassword().equals(queryString.get(UserDBConstants.COLUMN_PASSWORD)))
            return redirect("/user/login_failed.html");

        HttpResponse response = redirect("/index.html");
        response.header().putToHeaders("Set-Cookie", "logined=true; Path=/");

        return response;
    }
}
