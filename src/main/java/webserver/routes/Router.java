package webserver.routes;

import application.controller.UserController;
import http.HttpHeaders;
import http.HttpMethod;
import http.HttpStatus;
import http.request.HttpRequest;
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

            Object ret = UserController.create(userId, password, name, email);

            if(ret instanceof String) {
                String viewName = (String) ret;
                if(viewName.startsWith("redirect:")) {
                    Map<String, String> headers = new HashMap<>();
                    headers.put(HttpHeaders.LOCATION, viewName.substring("redirect:".length()));
                    return HttpResponse.Builder.newInstance()
                            .statusLine(StatusLine.of(request.getHttpVersion(), HttpStatus.SEE_OTHER))
                            .headers(HttpHeaders.of(headers))
                            .build();
                }
            }

            return HttpResponse.Builder.newInstance().build();
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
                    .statusLine(result.getStatusLine())
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
