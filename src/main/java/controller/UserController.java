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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
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
        methodMap.put("GET /user/list", this::list);
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

    private HttpResponse list(HttpRequest request) {
        if (!request.header().getCookie("logined").equals("true")) {
            return redirect("/user/login.html");
        }

        File file = new File(HttpResponseBody.STATIC_ROOT + "/user/list.html");

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            StringBuilder sb = new StringBuilder(new String(bytes));
            StringBuilder newsb = new StringBuilder();
            int i = 0;

            for (User user : DataBase.findAll()) {
                newsb.append("<tr>");
                newsb.append(String.format("<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>", ++i, user.getUserId(), user.getName(), user.getEmail()));
                newsb.append("</tr>");
            }
            sb.insert(sb.lastIndexOf("<tbody>"), newsb);

            HttpResponseBody responseBody = HttpResponseBody.createFromStringBuilder(sb);
            HttpResponseHeader responseHeader = new HttpResponseHeader("/user/list.html", HttpStatus.OK, responseBody.length());
            
            return new HttpResponse(responseHeader, responseBody);
        } catch (IOException e) {
            log.error("GET /user/list error");
            return errorPage();
        }
    }
}
