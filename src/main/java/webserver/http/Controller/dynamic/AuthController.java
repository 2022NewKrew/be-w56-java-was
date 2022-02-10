package webserver.http.Controller.dynamic;

import dto.UserLoginDto;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.constant.Route;
import webserver.http.Controller.HttpController;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseUtils;
import webserver.http.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

public class AuthController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final String WRONG_PASSWORD = "비밀번호를 잘못 입력했습니다";

    private final UserService userService = new UserService();


    @Override
    public boolean isValidRequest(HttpRequest request) {
        return Objects.equals(request.getUrl(), "/user/login");
 }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        Map<String, String> queries = HttpRequestUtils.parseQueryString(request.getHttpRequestBody());
        User findUser = userService.findUser(new UserLoginDto(queries.get("userId"), queries.get("password")));
        if (!Objects.equals(findUser.getPassword(), queries.get("password"))) {
            log.trace("invalid input: {}", findUser + WRONG_PASSWORD);
            return HttpResponseUtils.redirectTo(out, Route.LOGIN_FAILED.getPath(), "false");
        }
        if (request.getMethod() == Method.POST) {
            log.info("Login Success login user : {}", findUser);
            return HttpResponseUtils.redirectTo(out, Route.INDEX.getPath(), "true");
        }
        return HttpResponseUtils.notFound(out);
    }
}




