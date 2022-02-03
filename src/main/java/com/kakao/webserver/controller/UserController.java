package com.kakao.webserver.controller;

import com.kakao.http.header.HttpHeader;
import com.kakao.http.header.LocationHeader;
import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.request.HttpRoute;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import com.kakao.model.User;
import com.kakao.service.UserService;

import java.util.List;
import java.util.Set;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class UserController implements HttpController {
    private final UserService userService = UserService.getInstance();
    private final Set<HttpRoute> routeSet = Set.of(
            new HttpRoute(HttpMethod.POST, "/user/create")
    );

    @Override
    public boolean isValidRoute(HttpRoute route) {
        return routeSet.contains(route);
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request) {
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
