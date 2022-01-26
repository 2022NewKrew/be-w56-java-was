package adaptor.in.web.user;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import application.SignUpUserService;
import domain.user.User;
import infrastructure.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

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

    public HttpResponse handleWithResponse(HttpRequest httpRequest) throws FileNotFoundException, UriNotFoundException {
        Path path = httpRequest.getRequestPath();
        log.debug("User Controller: {}", path);

        try {
            if (path.matchHandler(REQUEST_MAPPING + "/create")) {
                return signUpWithResponse(httpRequest);
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        throw new UriNotFoundException();
    }

    private HttpResponse signUpWithResponse(HttpRequest request) throws IOException {
        log.debug("User SignUp Method Called!!");


        Map<String, String> pathVariables = request.getRequestPath().getVariables();
        User user = User.builder()
                .userId(pathVariables.get("userId"))
                .password(pathVariables.get("password"))
                .name(pathVariables.get("name"))
                .email(pathVariables.get("email"))
                .build();

        signUpUserService.signUp(user);

        return new HttpResponse(
                ResponseLine.valueOf(HttpStatus.OK),
                HttpHeader.of(Pair.of("Content-Type", "text/html; charset=utf-8")),
                HttpBody.valueOfFile("/index.html")
        );
    }
}
