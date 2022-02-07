package application.user;

import application.exception.user.AlreadyExistingUserException;
import application.in.user.SignUpUserUseCase;
import application.out.user.FindUserPort;
import application.out.user.SignUpUserPort;
import domain.user.User;

public class SignUpUserService implements SignUpUserUseCase {

    private final SignUpUserPort signUpUserPort;
    private final FindUserPort findUserPort;

    public SignUpUserService(SignUpUserPort signUpUserPort, FindUserPort findUserPort) {
        this.signUpUserPort = signUpUserPort;
        this.findUserPort = findUserPort;
    }

    @Override
    public void signUp(User user) {
        findUserPort.findByUserId(user.getUserId())
                .ifPresent(e -> {
                    throw new AlreadyExistingUserException();
                });

        signUpUserPort.addUser(user);
    }
}
