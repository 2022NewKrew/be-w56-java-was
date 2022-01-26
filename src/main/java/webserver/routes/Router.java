package webserver.routes;

import application.controller.UserController;
import http.HttpHeaders;
import http.HttpMethod;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.RequestParams;
import http.response.HttpResponse;
import http.response.ResponseBody;
import http.response.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    private static Router instance = null;

    private final Map<String, Function<HttpRequest, HttpResponse>> routeMap;

    private static final Logger log = LoggerFactory.getLogger(Router.class);

    private Router() {
        routeMap = new HashMap<>();
        routeMap.put(RouteUtils.makeKey(HttpMethod.POST.getCode(), "/user/create"), (request) -> {

            String userId = String.valueOf(request.getParameter("userId"));
            String password = String.valueOf(request.getParameter("password"));
            String name = String.valueOf(request.getParameter("name"));
            String email = String.valueOf(request.getParameter("email"));

            UserController.create(userId, password, name, email);

            String message = "회원 가입이 완료되었습니다.";
            Map<String, String> header = new HashMap<>();
            header.put(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8");
            header.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(message.getBytes(StandardCharsets.UTF_8).length));
            return HttpResponse.Builder.newInstance()
                    .headers(HttpHeaders.of(header))
                    .body(ResponseBody.of(message.getBytes(StandardCharsets.UTF_8)))
                    .build();
        });
    }

    public static Router getInstance() {
        if(instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public Boolean canRoute(HttpRequest httpRequest) {
        return routeMap.containsKey(RouteUtils.makeKey(httpRequest.getMethod().getCode(), httpRequest.getUrl()));
    }

    public HttpResponse route(HttpRequest httpRequest) {

        HttpResponse.Builder builder = HttpResponse.Builder.newInstance();

        try{
            String routeKey = RouteUtils.makeKey(httpRequest.getMethod().getCode(), httpRequest.getUrl());
            HttpResponse result = routeMap.get(routeKey).apply(httpRequest);
            builder = builder.headers(result.getHeaders())
                    .statusLine(StatusLine.of(httpRequest.getHttpVersion(), HttpStatus.OK))
                    .body(result.getResponseBody());

        } catch (Exception exception) {
            log.info("{} : {}", Router.class.getName(), exception.getMessage());
            return builder
                    .headers(HttpHeaders.of(new HashMap<>()))
                    .statusLine(StatusLine.of(httpRequest.getHttpVersion(), HttpStatus.INTERNAL_SERVER_ERROR))
                    .body(ResponseBody.of(new byte[0]))
                    .build();
        }

        return builder.build();
    }
}
