package com.kakao.webserver.controller;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.kakao.http.common.HttpCookie;
import com.kakao.http.header.ContentLengthHeader;
import com.kakao.http.header.ContentTypeHeader;
import com.kakao.http.header.HttpHeader;
import com.kakao.http.header.SetCookieHeader;
import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.request.HttpRoute;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import com.kakao.model.User;
import com.kakao.service.UserService;
import com.kakao.webserver.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.kakao.http.response.HttpResponseUtils.redirectTo;
import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class UserController implements HttpController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String LOGIN_FAILED_URL = "/user/login_failed.html";

    private final UserService userService = UserService.getInstance();
    private final Map<HttpRoute, Function<HttpRequest, HttpResponse>> routeMap = Map.ofEntries(
            Map.entry(new HttpRoute(HttpMethod.POST, "/user/create"), this::createUser),
            Map.entry(new HttpRoute(HttpMethod.POST, "/user/login"), this::login),
            Map.entry(new HttpRoute(HttpMethod.GET, "/user/list"), this::listUsers)
    );

    @Override
    public boolean isValidRoute(HttpRoute route) {
        return routeMap.containsKey(route);
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request) {
        HttpRoute route = new HttpRoute(request.getMethod(), request.getUrl().getPath());
        return routeMap.get(route)
                .apply(request);
    }

    private HttpResponse listUsers(HttpRequest request) {
        Optional<HttpCookie> loginCookie = request.findCookieByName("logined");
        if (loginCookie.isEmpty() || !loginCookie.get().value().equals("true")) {
            return redirectTo("/user/login.html");
        }
        List<User> userList = userService.findAll();
        String responseBody;
        try {
            responseBody = buildResponseBody(userList);
        } catch (IOException e) {
            logger.error("Building response body error.", e);
            return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.INTERNAL_SERVER_ERROR, null, null);
        }
        List<HttpHeader> headers = List.of(
                new ContentTypeHeader("list.html"),
                new ContentLengthHeader(responseBody.length())
        );
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.OK, headers, responseBody.getBytes());
    }

    private String buildResponseBody(List<User> userList) throws IOException {
        List<UserDto> userDtoList = buildUserDtoList(userList);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("./webapp/user/list.html");
        try (StringWriter writer = new StringWriter()) {
            mustache.execute(writer, Map.of("users", userDtoList)).flush();
            return writer.toString();
        }
    }

    private List<UserDto> buildUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            userDtoList.add(new UserDto(i + 1, userList.get(i)));
        }
        return userDtoList;
    }

    private HttpResponse login(HttpRequest request) {
        String userId = request.findBodyParam("userId");
        String password = request.findBodyParam("password");
        Optional<User> user = userService.findByUserId(userId);
        if (user.isEmpty() || !user.get().matchPassword(password)) {
            return loginFailed();
        }
        return loginSucceeded();
    }

    private HttpResponse loginSucceeded() {
        List<HttpHeader> headers = List.of(
                new SetCookieHeader(new HttpCookie("logined", "true").path("/"))
        );
        return redirectTo("/", headers);
    }

    private HttpResponse loginFailed() {
        List<HttpHeader> headers = List.of(
                new SetCookieHeader(new HttpCookie("logined", "false").path("/"))
        );
        return redirectTo(LOGIN_FAILED_URL, headers);
    }

    private HttpResponse createUser(HttpRequest request) {
        User user = buildUser(request);
        userService.addUser(user);
        return redirectTo("/");
    }

    private User buildUser(HttpRequest request) {
        String userId = request.findBodyParam("userId");
        String password = request.findBodyParam("password");
        String name = request.findBodyParam("name");
        String email = request.findBodyParam("email");
        return new User(userId, password, name, email);
    }
}
