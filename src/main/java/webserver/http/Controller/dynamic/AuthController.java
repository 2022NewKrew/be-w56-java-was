package webserver.http.Controller.dynamic;

import dto.UserLoginDto;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.http.Controller.HttpController;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;
import webserver.http.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

public class AuthController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService = new UserService();


    @Override
    public boolean isValidRequest(HttpRequest request) {
        return request.getUrl().contains("user/login");
 }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        Map<String, String> queries = HttpRequestUtils.parseQueryString(request.getHttpRequestBody());
        User findUser = userService.findUser(new UserLoginDto(queries.get("userId"), queries.get("password")));
        switch (request.getMethod()) {
            case POST:
                if (findUser != null && !Objects.equals(findUser.getPassword(), queries.get("password"))) {
                    log.info("Login Failed due to password tried login user : {}", findUser);
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setCookie("false")
                            .setRedirect("/user/login_failed.html")
                            .build();
                }
                if (findUser != null && Objects.equals(findUser.getPassword(), queries.get("password"))) {
                    log.info("Login Success login user : {}", findUser);
                    return new HttpResponse.Builder(out)
                            .setHttpStatus(HttpStatus._302)
                            .setCookie("true")
                            .setRedirect("/index.html")
                            .build();
                }
            default:
                return new HttpResponse.Builder(out)
                        .setHttpStatus(HttpStatus._404)
                        .setRedirect("/index.html")
                        .build();
        }
    }
}



