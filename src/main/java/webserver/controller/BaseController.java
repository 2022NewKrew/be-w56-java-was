package webserver.controller;

import http.exception.UnsupportedHttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.RequestHandler;

import java.util.Map;

@Slf4j
public class BaseController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) {
        // FIXME
        if (request.getHeaders().containsName("cookie")) {
            try {
                Map<String, String> cookies = HttpRequestUtils.parseCookies(request.getHeaders().getHeaders().get("cookie"));
                User user = RequestHandler.getSessionUser(Long.parseLong(cookies.get("sessionId")));

                log.debug("세션 유저: {}", user);
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
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
