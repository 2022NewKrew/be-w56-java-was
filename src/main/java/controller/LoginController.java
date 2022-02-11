package controller;

import static webserver.AppConfig.appConfig;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import java.util.Optional;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpResponseUtils;

public class LoginController extends AbstractController{

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.set200(appConfig.HOME, HttpResponseUtils.acceptType(appConfig.HOME));
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        String account = httpRequest.getBody().get("userId");
        String password = httpRequest.getBody().get("password");
        String name = httpRequest.getBody().get("name");
        String email = httpRequest.getBody().get("email");
        Optional<User> user = userService.login(new User(account, password, name, email));
        if (user.isPresent()) {
            httpResponse.set302(appConfig.HOME);
            httpResponse.addCookie("logined", "true");
            log.info("{} 로그인 성공", account);
            return;
        }
        httpResponse.set200(httpRequest.getPath()+".html", HttpResponseUtils.acceptType(httpRequest.getPath()));
    }
}
