package webserver.controller;

import http.cookie.Cookie;
import http.exception.NotFound;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.RequestHandler;

import java.util.List;

@Slf4j
public class UserListController extends BaseController {

    @Override
    public HttpResponse get(HttpRequest request) {
        // TODO: Interceptor로 구현
        try {
            List<Cookie> cookies = request.getCookies();
            Long sessionId = cookies.stream()
                    .filter(x -> x.getName().equals("sessionId"))
                    .findAny()
                    .map(x -> Long.parseLong(x.getValue()))
                    .orElseThrow(NotFound::new);
            User user = RequestHandler.getSessionUser(sessionId);

            log.debug("세션 유저: {}", user);

            return HttpResponse.ok("/user/list.html");
        } catch (Exception e) {
            log.debug(e.getMessage());
            return HttpResponse.redirect("/user/login.html");
        }
    }
}
