package webserver.controller;

import http.cookie.Cookie;
import http.exception.NotFound;
import http.exception.UnsupportedHttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.RequestHandler;

import java.util.List;

@Slf4j
public class BaseController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) {
        // FIXME
        try {
            List<Cookie> cookies = request.getCookies();
            Long sessionId = cookies.stream()
                    .filter(x -> x.getName().equals("sessionId"))
                    .findAny()
                    .map(x -> Long.parseLong(x.getValue()))
                    .orElseThrow(NotFound::new);
            User user = RequestHandler.getSessionUser(sessionId);

            log.debug("세션 유저: {}", user);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        switch (request.getMethod()) {
            case GET:
                return get(request);

            case POST:
                return post(request);

            case PUT:
            case DELETE:
            case CONNECT:
            case OPTIONS:
            case TRACE:
            case PATCH:
            default:
                throw new UnsupportedHttpMethod();
        }
    }

    public HttpResponse get(HttpRequest request) {
        return HttpResponse.ok(request.getUri());
    }

    public HttpResponse post(HttpRequest request) {
        return HttpResponse.ok(request.getUri());
    }
}
