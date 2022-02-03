package app;

import app.user.adapter.in.SignUpController;
import app.user.application.port.SignUpService;
import app.user.application.port.in.CreateUserUseCase;

public class AppConfig {

    public RootController rootController() {
        return new RootController();
    }

    public SignUpController signUpController() {
        return new SignUpController(createUserUseCase());
    }

    private CreateUserUseCase createUserUseCase() {
        return new SignUpService();
    }

}
