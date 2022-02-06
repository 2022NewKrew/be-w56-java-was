package user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.domain.User;
import user.dto.request.SignUpRequest;
import user.service.UserService;
import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.domain.HttpStatus;
import webserver.domain.Request;
import webserver.domain.Response;

@Controller
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/create", method = "GET")
    public Response signUpUser(Request request) {
        String userId = request.getQueryStringAttribute("userId");
        String password = request.getQueryStringAttribute("password");
        String name = request.getQueryStringAttribute("name");
        String email = request.getQueryStringAttribute("email");
        SignUpRequest signUpRequest = new SignUpRequest(userId, password, name, email);

        User user = signUpRequest.toUser();
        userService.save(user);

        logger.info("회원 가입: {}", userId);
        return Response.createResponse(HttpStatus.FOUND, "/index.html");
    }
}
