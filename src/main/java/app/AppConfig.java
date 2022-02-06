package app;

import app.user.adapter.in.LoginController;
import app.user.adapter.in.SignUpController;
import app.user.adapter.out.UserRepository;
import app.user.application.LoginService;
import app.user.application.SignUpService;
import app.user.application.port.in.CreateUserUseCase;
import app.user.application.port.in.LoginUseCase;
import app.user.application.port.out.LoadUserPort;

public class AppConfig {

    public RootController rootController() {
        return new RootController();
    }

    public SignUpController signUpController() {
        return new SignUpController(createUserUseCase());
    }

    public LoginController loginController() {
        return new LoginController(loginUseCase());
    }

    private CreateUserUseCase createUserUseCase() {
        return new SignUpService();
    }

    private LoginUseCase loginUseCase() {
        return new LoginService(loadUserRepository());
    }

    private LoadUserPort loadUserRepository() {
        return new UserRepository();
    }

}
