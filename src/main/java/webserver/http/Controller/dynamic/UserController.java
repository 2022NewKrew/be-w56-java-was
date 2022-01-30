package webserver.http.Controller.dynamic;

import dto.UserSignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.HttpRequestUtils;
import webserver.http.Controller.HttpController;
import webserver.http.Controller.StaticController;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;
import webserver.http.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class UserController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    private final UserService userService = new UserService();

    @Override
    public boolean isValidRequest(HttpRequest request) {
        System.out.println(request.getUrl());
        return request.getUrl().contains("user/create");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        if (request.getMethod() == Method.GET) {
            String queries = request.getUrl().split(Constants.QUESTION)[1];
            Map<String, String> queriesMap = HttpRequestUtils.parseQueryString(queries);
            userService.join(new UserSignUpDto(queriesMap.get("userId"), queriesMap.get("password"), queriesMap.get("name"), queriesMap.get("email")));
            log.debug("UserController handleRequest User joined by GET");
            log.debug("UserController handleRequest GET queries : {} ", queries);
            return new HttpResponse.Builder(out)
                    .setHttpStatus(HttpStatus._302)
                    .setRedirect("/index.html")
                    .build();
        }

        if (request.getMethod() == Method.POST) {
            System.out.println(request.getHttpRequestBody());
            Map<String, String> queries = HttpRequestUtils.parseQueryString(request.getHttpRequestBody());
            userService.join(new UserSignUpDto(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email")));
            log.debug("UserController handleRequest User joined by POST");
            log.debug("UserController handleRequest POST queries : {} ", queries);
            return new HttpResponse.Builder(out)
                    .setHttpStatus(HttpStatus._302)
                    .setRedirect("/index.html")
                    .build();
        }
        return null;
    }
}
