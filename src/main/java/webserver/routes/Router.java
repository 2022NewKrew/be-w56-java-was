package webserver.routes;

import application.controller.UserController;
import application.dto.SignUpRequest;
import http.*;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    private static Router instance = null;

    private final Map<String, Function<HttpRequest, HttpResponse>> routeMap;

    private static final Logger log = LoggerFactory.getLogger(Router.class);

    private Router() {
        routeMap = new HashMap<>();
        routeMap.put(RouteUtils.makeKey(HttpMethod.POST.getCode(), "/user/create"), this::create);
        routeMap.put(RouteUtils.makeKey(HttpMethod.POST.getCode(), "/user/login"), this::login);
    }

    private HttpResponse create(HttpRequest request) {
        SignUpRequest signUpRequest = SignUpRequest.Builder.newInstance()
                .userId(String.valueOf(request.getParameter("userId")))
                .password(String.valueOf(request.getParameter("password")))
                .name(String.valueOf(request.getParameter("name")))
                .email(String.valueOf(request.getParameter("email")))
                .build();
        return processReturnType(request, UserController.create(signUpRequest));
    }

    private HttpResponse login(HttpRequest request) {
        String userId = String.valueOf(request.getParameter("userId"));
        String password = String.valueOf(request.getParameter("password"));
        Cookies cookies = new Cookies();
        HttpResponse response = processReturnType(request, UserController.login(userId, password, cookies));

        MultiValueMap<String, String> headers = response.getHeaders().getHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookies.toHeaderString());

        return HttpResponse.Builder.newInstance()
                .statusLine(response.getStatusLine())
                .headers(HttpHeaders.of(headers))
                .body(response.getBody())
                .build();
    }

    private HttpResponse processReturnType(HttpRequest request, Object ret) {
        if(ret instanceof String) {
            String viewName = (String) ret;
            if(viewName.startsWith("redirect:")) {
                MultiValueMap<String, String> headers = new MultiValueMap<>();
                headers.add(HttpHeaders.LOCATION, viewName.substring("redirect:".length()));

                return HttpResponse.Builder.newInstance()
                        .statusLine(StatusLine.of(request.getHttpVersion(), HttpStatus.SEE_OTHER))
                        .headers(HttpHeaders.of(headers))
                        .build();
            }
        }
        return HttpResponse.Builder.newInstance().build();
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
                    .body(result.getBody());

        } catch (Exception exception) {
            log.info("{} : {}", Router.class.getName(), exception.getMessage());
            return builder
                    .headers(HttpHeaders.of(new MultiValueMap<>()))
                    .statusLine(StatusLine.of(httpRequest.getHttpVersion(), HttpStatus.INTERNAL_SERVER_ERROR))
                    .body(HttpBody.of(new byte[0]))
                    .build();
        }

        return builder.build();
    }
}
