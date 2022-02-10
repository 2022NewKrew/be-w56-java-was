package app.user.adapter.in;

import app.user.application.port.in.CreateUserUseCase;
import app.user.application.port.in.SignUpUserDto;
import app.user.domain.User;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Model;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpControllable;
import webserver.util.HttpRequestUtils;

public class SignUpController implements HttpControllable {

    public static final String path = "/user/create";
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    private final CreateUserUseCase createUserUseCase;

    public SignUpController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    public String call(HttpRequest request, HttpResponse response, Model model) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getBody());
        SignUpUserDto signUpUserDto = new SignUpUserDto(
            params.get("userId"),
            params.get("password"),
            params.get("name"),
            params.get("email")
        );

        User user = this.createUserUseCase.signUp(signUpUserDto);
        logger.info("[회원가입] :" + user.getUserId());
        return "/index";
    }
}
