package webserver.http.Controller.dynamic;

import dto.UserSignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.HttpRequestUtils;
import webserver.http.Controller.HttpController;
import webserver.http.Controller.StaticController;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;
import webserver.http.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class UserController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService = new UserService();

    @Override
    public boolean isValidRequest(HttpRequest request) {
        return request.getUrl().contains("user/create") || request.getUrl().contains("user/list");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        if (request.getUrl().contains("user/create")) {
            switch (request.getMethod()) {
                case GET:
                    String queries = request.getUrl().split(Constants.QUESTION)[1];
                    Map<String, String> queriesMap = HttpRequestUtils.parseQueryString(queries);
                    userService.join(new UserSignUpDto(queriesMap.get("userId"), queriesMap.get("password"), queriesMap.get("name"), queriesMap.get("email")));
                    log.debug("UserController handleRequest User joined by GET");
                    log.debug("UserController handleRequest GET queries : {} ", queries);
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setRedirect("/index.html")
                            .build();
                case POST:
                    Map<String, String> queriesPost = HttpRequestUtils.parseQueryString(request.getHttpRequestBody());
                    userService.join(new UserSignUpDto(queriesPost.get("userId"), queriesPost.get("password"), queriesPost.get("name"), queriesPost.get("email")));
                    log.debug("UserController handleRequest User joined by POST");
                    log.debug("UserController handleRequest POST queries : {} ", queriesPost);
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setRedirect("/index.html")
                            .build();
                default:
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._404)
                            .setRedirect("/index.html")
                            .build();
            }
        } else if (request.getUrl().contains("user/list")) {
            switch (request.getMethod()) {
                case GET:
                    log.debug("UserController handleRequest list by GET");
                    String cookies = request.getHttpRequestHeader().getHeaders().get("Cookie");
                    Map<String, String> parsedCookies = HttpRequestUtils.parseCookies(cookies);
                    if (Objects.equals(parsedCookies.get("logined"), "true")) {
                        System.out.println("합격");

                    } else {
                        return new HttpResponse.Builder(out)
                                .setHttpStatus(HttpStatus._302)
                                .setRedirect("/user/login.html")
                                .build();
                    }
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setRedirect("/index.html")
                            .build();
            }
        }
        return new HttpResponse.Builder(out)
                .setHttpStatus(HttpStatus._404)
                .setRedirect("/index.html")
                .build();
    }
}

