package app.user.application;

import app.user.adapter.out.UserRepository;
import app.user.application.port.in.LoginUserDto;
import app.user.domain.User;
import app.user.exception.WrongPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LoginServiceTest {

    private static final UserRepository userRepository = new UserRepository();
    private static final LoginService loginService = new LoginService(userRepository);
    private static final User user = new User("myId", "myPassword", "myName", "myEmail");

    @BeforeAll
    static void init() {
        userRepository.save(user);
    }

    @Test
    void loginSuccess() {
        LoginUserDto correctLoginDto = new LoginUserDto("myId", "myPassword");
        Assertions.assertEquals(user, loginService.login(correctLoginDto));
    }

    @Test
    void loginFail() {
        LoginUserDto wrongLoginDto = new LoginUserDto("myId", "wrongPassword");
        Assertions.assertThrows(WrongPasswordException.class,
            () -> loginService.login(wrongLoginDto));
    }
}