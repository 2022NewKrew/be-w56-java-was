package webserver.controller;

import annotation.GetMapping;
import annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import model.*;
import webserver.service.UserService;
import webserver.web.request.Request;
import webserver.web.response.Response;

@Slf4j
@annotation.Controller
public class UserController extends BaseController {

    private final UserService userService = UserService.getInstance();
    private static final UserController userController = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return userController;
    }

    @Override
    public boolean isSupply(Request request) {
        String target = request.getMethod().toString() + " " + request.getUrl().toString();
        return supplyUrl.keySet().stream().anyMatch(key -> key.equals(target));
    }

    @Override
    public Response handle(Request request, OutputStream out) throws IOException {
        Response response;
        try {
            String target = request.getMethod().toString() + " " + request.getUrl().toString();
            Optional<String> result = supplyUrl.keySet().stream()
                    .filter(key -> key.equals(target))
                    .findAny();
            log.info("matching = {}", result);
            supplyUrl.get(result.get()).invoke(this, request);
            log.info("{}", supplyUrl.get(result.get()).getName());

    @PostMapping(url = "/user/login")
    public String loginUser(String userId, String password, Request request, Response.ResponseBuilder builder) {
        if (request.inquireHeaderData("Cookie").contains("logined=true")) {
            log.info("이미 로그인된 유저");
            return "redirect:/index.html";
        }
        if (userService.loginUser(userId, password)) {
            builder.setCookie("logined=true; Path=/");
            log.info("로그인 성공");
            return "redirect:/index.html";
        }
        builder.setCookie("logined=false");
        log.info("로그인 실패");
        return "redirect:/user/login_failed.html";
    }

    private User setUserInformation(String userId, String name, String password, String email) {
        UserId joinUserId = new UserId(userId);
        Name userName = new Name(name);
        Password userPassword = new Password(password);
        Email userEmail = new Email(email);
        return new User(joinUserId, userPassword, userName, userEmail);
    }
}
