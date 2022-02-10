package app.user.application;

import app.user.adapter.out.UserRepository;
import app.user.application.port.in.SignUpUserDto;
import app.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SignUpServiceTest {

    private final SignUpService signUpService = new SignUpService(new UserRepository());

    @Test
    void signUp() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
            "myId",
            "myPassword",
            "myName",
            "myEmail@example.com");
        User user = signUpService.signUp(signUpUserDto);
        
        Assertions.assertEquals(signUpUserDto.getUserId(), user.getUserId());
        Assertions.assertEquals(signUpUserDto.getPassword(), user.getPassword());
        Assertions.assertEquals(signUpUserDto.getName(), user.getName());
        Assertions.assertEquals(signUpUserDto.getEmail(), user.getEmail());
    }
}