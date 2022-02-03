package app.user.adapter.in;

import app.user.application.port.in.CreateUserUseCase;
import app.user.application.port.in.SignUpUserDto;
import app.user.domain.User;
import com.google.common.net.HttpHeaders;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.HttpControllable;
import webserver.util.HttpRequestUtils;

public class SignUpController implements HttpControllable {

    public static final String path = "/user";
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    private final CreateUserUseCase createUserUseCase;

    public SignUpController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    public void call(HttpRequest request, HttpResponse response) {
        if (request.getUri().startsWith(path + "/create")) {
            logger.info("call signUp()");
            signUp(request, response);
        }
    }

    private void signUp(HttpRequest request, HttpResponse response) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getBody());
        SignUpUserDto signUpUserDto = new SignUpUserDto(
            params.get("userId"),
            params.get("password"),
            params.get("name"),
            params.get("email")
        );

        User user = this.createUserUseCase.signUp(signUpUserDto);
        logger.info("sign up :" + user.getUserId());
        response.setStatus(HttpResponseStatus.FOUND);
        response.headers()
            .set(HttpHeaders.LOCATION, WebServerConfig.ENDPOINT + WebServerConfig.ENTRY_FILE);
    }
}
