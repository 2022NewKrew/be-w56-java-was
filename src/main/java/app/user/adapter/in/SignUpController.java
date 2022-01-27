package app.user.adapter.in;

import app.user.application.port.in.CreateUserUseCase;
import app.user.application.port.in.SignUpUserDto;
import app.user.domain.User;
import java.util.Map;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class SignUpController {

    public static final String path = "/user";
    private final CreateUserUseCase createUserUseCase;

    public SignUpController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    public HttpResponse call(HttpRequest request, HttpResponse response) {
        if (request.getUri().startsWith(path + "/create")) {
            return signUp(request, response);
        }
        return response;
    }

    private HttpResponse signUp(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getParams().getParameters();
        SignUpUserDto signUpUserDto = new SignUpUserDto(
            params.get("userId"),
            params.get("password"),
            params.get("name"),
            params.get("email")
        );

        User user = this.createUserUseCase.signUp(signUpUserDto);
        return response;
    }
}
