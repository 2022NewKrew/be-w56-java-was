package webserver.http.Controller.dynamic;

import dto.UserSignUpDto;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.constant.Http;
import util.constant.Parser;
import webserver.http.Controller.HttpController;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseUtils;
import webserver.http.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class UserController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService = new UserService();

    @Override
    public boolean isValidRequest(HttpRequest request) {
        return Objects.equals(request.getUrl(), "/user/create") || Objects.equals(request.getUrl(), "/user/list.html");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        log.debug("UserController request url {}", request.getUrl());
        if (Objects.equals(request.getUrl(), "/user/create") && request.getMethod() == Method.GET) {
            return userCreateByGet(request, out);
        }
        if (Objects.equals(request.getUrl(), "/user/create") && request.getMethod() == Method.POST) {
            return userCreateByPost(request, out);
        }
        if (Objects.equals(request.getUrl(), "/user/list.html") && request.getMethod() == Method.GET) {
            return userList(request, out);
        }
        return HttpResponseUtils.notFound(out);
    }

    private HttpResponse userCreateByGet(HttpRequest request, OutputStream out) {
        String queries = request.getUrl().split(Parser.QUESTION)[1];
        Map<String, String> queriesMap = HttpRequestUtils.parseQueryString(queries);
        userService.join(new UserSignUpDto(queriesMap.get("userId"), queriesMap.get("password"), queriesMap.get("name"), queriesMap.get("email")));
        log.debug("UserController handleRequest User joined by {}", request.getMethod());
        log.debug("UserController handleRequest queries : {} ", queries);
        return HttpResponseUtils.redirectTo(out);
    }

    private HttpResponse userCreateByPost(HttpRequest request, OutputStream out) {
        Map<String, String> queries = HttpRequestUtils.parseQueryString(request.getHttpRequestBody());
        userService.join(new UserSignUpDto(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email")));
        log.debug("UserController handleRequest User joined by {}", request.getMethod());
        log.debug("UserController handleRequest queries : {} ", queries);
        return HttpResponseUtils.redirectTo(out);
    }

    private HttpResponse userList(HttpRequest request, OutputStream out) throws IOException{
        log.debug("UserController handleRequest userList");
        String cookies = request.getHttpRequestHeader().getHeaders().get("Cookie");
        Map<String, String> parsedCookies = HttpRequestUtils.parseCookies(cookies);
        if (Objects.equals(parsedCookies.get(Http.COOKIE_LOGINED_KEY), "true")) {
            Collection<User> users = userService.findUsers();
            StringBuilder sb = new StringBuilder();
            int index = 3;
            for (User user : users) {
                sb.append("<tr>");
                sb.append("<th scope=\"row\">" + index + "</th> <td>" + user.getUserId() + "</td> <td> " + user.getName() + "</td> <td> " + user.getEmail() + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
                sb.append("</tr>");
                index++;
            }
            return HttpResponseUtils.ok(out, request, sb);
        }
        return HttpResponseUtils.redirectTo(out, "/user/login.html");
    }
}

