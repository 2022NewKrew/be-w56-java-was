package adaptor.in.web.user;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import application.in.SignUpUserUseCase;
import domain.user.User;
import infrastructure.model.*;
import infrastructure.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class UserController {

    private static final String REQUEST_MAPPING = "/user";
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final SignUpUserUseCase signUpUserUseCase;

    public UserController(SignUpUserUseCase signUpUserUseCase) {
        this.signUpUserUseCase = signUpUserUseCase;
    }

    public HttpResponse handleWithResponse(HttpRequest httpRequest) throws FileNotFoundException, UriNotFoundException {
        Path path = httpRequest.getRequestPath();
        log.debug("User Controller: {}", path);

        try {
            if (path.matchHandler(REQUEST_MAPPING + "/create")) {
                return signUp(httpRequest);
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        throw new UriNotFoundException();
    }

    private HttpResponse signUp(HttpRequest request) throws IOException {
        log.debug("User SignUp Method Called!!");

        Map<String, String> body = HttpRequestUtils.parseBody(((HttpStringBody) request.getRequestBody()).getValue());
        User user = User.builder()
                .userId(body.get("userId"))
                .password(body.get("password"))
                .name(body.get("name"))
                .email(body.get("email"))
                .build();

        signUpUserUseCase.signUp(user);

        return new HttpResponse(
                ResponseLine.valueOf(HttpStatus.OK),
                HttpHeader.of(Pair.of("Content-Type", "text/html; charset=utf-8")),
                HttpByteArrayBody.setFile("/index.html")
        );
    }
}
