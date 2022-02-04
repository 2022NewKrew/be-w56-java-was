package com.kakao.webserver.controller;

import com.kakao.http.common.HttpCookie;
import com.kakao.http.header.HttpHeader;
import com.kakao.http.header.LocationHeader;
import com.kakao.http.header.SetCookieHeader;
import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.request.HttpRoute;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import com.kakao.model.User;
import com.kakao.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class UserController implements HttpController {
    private static final String LOGIN_FAILED_URL = "/user/login_failed.html";

    private final UserService userService = UserService.getInstance();
    private final Map<HttpRoute, Function<HttpRequest, HttpResponse>> routeMap = Map.ofEntries(
            Map.entry(new HttpRoute(HttpMethod.POST, "/user/create"), this::createUser),
            Map.entry(new HttpRoute(HttpMethod.POST, "/user/login"), this::login)
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
                new LocationHeader("/"),
                new SetCookieHeader(new HttpCookie("logined", "true").path("/"))
        );
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.FOUND, headers, null);
    }

    private HttpResponse loginFailed() {
        List<HttpHeader> headers = List.of(
                new LocationHeader(LOGIN_FAILED_URL),
                new SetCookieHeader(new HttpCookie("logined", "false").path("/"))
        );
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.FOUND, headers, null);
    }

    private HttpResponse createUser(HttpRequest request) {
        User user = buildUser(request);
        userService.addUser(user);

        List<HttpHeader> headers = List.of(new LocationHeader("/"));
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.FOUND, headers, null);
    }

    private User buildUser(HttpRequest request) {
        String userId = request.findBodyParam("userId");
        String password = request.findBodyParam("password");
        String name = request.findBodyParam("name");
        String email = request.findBodyParam("email");
        return new User(userId, password, name, email);
    }
}
