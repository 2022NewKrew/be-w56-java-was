package webserver.routes;

import application.controller.UserController;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.RequestParams;
import http.response.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    private static Router instance = null;

    private final Map<String, Function<HttpRequest, HttpResponse>> routeMap;

    private Router() {
        routeMap = new HashMap<>();
        routeMap.put(RouteUtils.makeKey("GET", "/user/create"), (request) -> {
            RequestParams params = request.getParams();
            UserController.create(params.getValue("userId"), params.getValue("password"), params.getValue("name"), params.getValue("email"));

            HttpResponse httpResponse = HttpResponse.create();
            String message = "회원 가입이 완료되었습니다.";
            httpResponse.updateProtocolVersion(request.getHttpVersion());
            httpResponse.updateStatus(HttpStatus.OK);
            httpResponse.updateBody(message.getBytes(StandardCharsets.UTF_8));
            httpResponse.addHeader("Content-Type", "text/html;charset=utf-8");
            httpResponse.addHeader("Content-Length", String.valueOf(message.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        });
    }

    public static Router getInstance() {
        if(instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public Boolean canRoute(HttpRequest httpRequest) {
        return routeMap.containsKey(RouteUtils.makeKey(httpRequest.getMethod(), httpRequest.getUrl()));
    }

    public HttpResponse route(HttpRequest httpRequest) {
        return routeMap.get(RouteUtils.makeKey(httpRequest.getMethod(), httpRequest.getUrl()))
                .apply(httpRequest);
    }
}
