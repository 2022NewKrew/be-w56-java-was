package application.user;

import application.in.user.SignUpUserUseCase;
import application.out.user.SignUpUserPort;
import domain.user.User;

public class SignUpUserService implements SignUpUserUseCase {

    private final SignUpUserPort signUpUserPort;

    public SignUpUserService(SignUpUserPort signUpUserPort) {
        this.signUpUserPort = signUpUserPort;
    }

    @Override
    public void signUp(User user) {
        signUpUserPort.addUser(user);
    }
}
