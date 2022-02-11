package controller;

import static webserver.AppConfig.appConfig;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpResponseUtils;

public class RegisterController extends AbstractController{

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private final UserService userService;

    public RegisterController(UserService userService) {
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
        userService.save(new User(account, password, name, email));
        log.info("{} 회원가입 성공", account);
        httpResponse.set302(appConfig.HOME);
    }
}
