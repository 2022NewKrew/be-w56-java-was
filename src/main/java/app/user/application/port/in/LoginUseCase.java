package app.user.application.port.in;

import app.user.domain.User;

public interface LoginUseCase {

    User login(LoginUserDto loginUserDto);

}
