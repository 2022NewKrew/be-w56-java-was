package adaptor.in.web.user;

import adaptor.in.web.exception.UriNotFoundException;
import application.SignUpUserService;
import domain.user.User;
import infrastructure.model.ContentType;
import infrastructure.model.HttpRequest;
import infrastructure.model.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static infrastructure.util.ResponseHandler.response200Body;
import static infrastructure.util.ResponseHandler.response200Header;

public class UserController {

    private static final String REQUEST_MAPPING = "/user";
    private static final UserController INSTANCE = new UserController();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final SignUpUserService signUpUserService = SignUpUserService.getINSTANCE();

    private UserController() {
    }

    public static UserController getInstance() {
        return INSTANCE;
    }

    public void handle(DataOutputStream dos, HttpRequest httpRequest) {
        Path path = httpRequest.getRequestPath();
        log.debug("User Controller: {}", path);
        if (path.matchHandler(REQUEST_MAPPING + "/create")) {
            signUp(dos, httpRequest);
        }
    }

    private void signUp(DataOutputStream dos, HttpRequest httpRequest) {
        log.debug("User SignUp Page Request!");

        Map<String, String> pathVariables = httpRequest.getRequestPath().getVariables();
        User user = User.builder()
                .userId(pathVariables.get("userId"))
                .password(pathVariables.get("password"))
                .name(pathVariables.get("name"))
                .email(pathVariables.get("email"))
                .build();

        signUpUserService.signUp(user);

        try {
            byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response200Header(dos, ContentType.HTML.getMimeType(), body.length);
            response200Body(dos, body);
        } catch (IOException e) {
            throw new UriNotFoundException();
        }
    }
}
