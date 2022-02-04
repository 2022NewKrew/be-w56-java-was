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
import java.nio.file.Files;
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
        methodMap.put("POST /user/create", this::create);
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
    public HttpResponse processDynamic(HttpRequest request) {
        final HttpRequestLine requestLine = request.line();

        if (methodMap.containsKey(requestLine.methodAndPath())) {
            log.debug("{} called", requestLine.methodAndPath());
            return methodMap.get(requestLine.methodAndPath()).apply(request);
        } else {
            log.debug("{} {} redirect to error page", requestLine.method(), requestLine.path());
            return errorPage();
        }
    }

    /**
     *  POST /user/create
     *  request body 에서 정보를 읽어서 새로운 User 정보 생성 후
     *  DataBase 에 저장
     */
    private HttpResponse create(HttpRequest request) {
        HttpRequestBody requestBody = request.body();
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody.content());

        User newUser = new User(
                queryString.get(UserDBConstants.COLUMN_USER_ID),
                queryString.get(UserDBConstants.COLUMN_PASSWORD),
                queryString.get(UserDBConstants.COLUMN_NAME),
                queryString.get(UserDBConstants.COLUMN_EMAIL));

        if (DataBase.findUserById(newUser.getUserId()) != null)
            return redirect("/user/form_failed.html");

        DataBase.addUser(newUser);
        return redirect("/index.html");
    }

    /**
     *  POST /user/login
     *  id가 존재하지 않거나 password 가 일치하지 않으면 login_failed.html 로 redirect
     *  로그인 성공 시, cookie 설정 후 응답 반환
     */
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

    /**
     *  GET /user/list
     *  사용자 리스트 페이지 접근 시, 동적으로 페이지를 만들어서 응답 반환
     */
    private HttpResponse list(HttpRequest request) {
        if (!"true".equals(request.header().getCookie("logined"))) {
            return redirect("/user/login.html");
        }

        File file = new File(HttpResponseBody.STATIC_ROOT + "/user/list.html");

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            StringBuilder sb = new StringBuilder(new String(bytes));
            StringBuilder newsb = new StringBuilder();
            int row = 0;

            for (User user : DataBase.findAll()) {
                newsb.append("<tr>");
                newsb.append(String.format("<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td>",
                        ++row, user.getUserId(), user.getName(), user.getEmail()));
                newsb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
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
