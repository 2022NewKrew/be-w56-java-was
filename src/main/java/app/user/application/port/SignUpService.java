package app.user.application.port;

import app.user.application.port.in.CreateUserUseCase;
import app.user.application.port.in.SignUpUserDto;
import app.user.domain.User;

public class SignUpService implements CreateUserUseCase {

    public SignUpService() {
    }

    @Override
    public User signUp(SignUpUserDto signUpUserDto) {
        return new User(
            signUpUserDto.getUserId(),
            signUpUserDto.getPassword(),
            signUpUserDto.getName(),
            signUpUserDto.getEmail()
        );
    }

}
