package application.user;

import application.exception.user.NonExistsUserIdException;
import application.in.user.LoginUseCase;
import application.out.user.FindUserPort;
import domain.user.User;

public class LoginService implements LoginUseCase {

    private final FindUserPort findUserPort;

    public LoginService(FindUserPort findUserPort) {
        this.findUserPort = findUserPort;
    }

    @Override
    public boolean login(String userId, String password) {
        User user = findUserPort.findByUserId(userId)
                .orElseThrow(NonExistsUserIdException::new);

        return user.isPasswordSame(password);
    }
}
