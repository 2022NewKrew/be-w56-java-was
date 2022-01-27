package app.user.application.port.in;

import app.user.domain.User;

public interface CreateUserUseCase {

    User signUp(SignUpUserDto signUpUserDto);

}
