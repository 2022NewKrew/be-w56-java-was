package app.user.application;

import app.user.application.port.in.LoginUseCase;
import app.user.application.port.in.LoginUserDto;
import app.user.application.port.out.LoadUserPort;
import app.user.domain.User;
import app.user.exception.WrongPasswordException;

public class LoginService implements LoginUseCase {

    private final LoadUserPort loadUserPort;

    public LoginService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    @Override
    public User login(LoginUserDto loginUserDto) {
        User user = loadUserPort.findUserById(loginUserDto.getUserId());
        boolean passwordMatched = user.isPasswordMatch(loginUserDto.getPassword());
        if (!passwordMatched) {
            throw new WrongPasswordException();
        }
        return user;
    }
}