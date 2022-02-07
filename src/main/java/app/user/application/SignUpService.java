package app.user.application;

import app.user.application.port.in.CreateUserUseCase;
import app.user.application.port.in.SignUpUserDto;
import app.user.application.port.out.SaveUserPort;
import app.user.domain.User;

public class SignUpService implements CreateUserUseCase {

    private final SaveUserPort saveUserPort;

    public SignUpService(SaveUserPort saveUserPort) {
        this.saveUserPort = saveUserPort;
    }

    @Override
    public User signUp(SignUpUserDto signUpUserDto) {
        User user = new User(
            signUpUserDto.getUserId(),
            signUpUserDto.getPassword(),
            signUpUserDto.getName(),
            signUpUserDto.getEmail()
        );
        saveUserPort.save(user);
        return user;
    }

}
