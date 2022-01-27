package application.in.user;

import application.exception.user.NonExistsUserIdException;
import application.out.user.FindUserPort;
import application.user.LoginService;
import domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    LoginUseCase loginUseCase;

    @Mock
    FindUserPort findUserPort;

    @BeforeEach
    void injectMock() {
        loginUseCase = new LoginService(findUserPort);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(findUserPort);
    }

    @DisplayName("사용자는 아이디와 패스워드로 로그인을 할 수 있다.")
    @Test
    void checkLogin() {
        String userId = "userId";
        String password = "password";
        User user = User.builder()
                .userId(userId)
                .password(password)
                .name("name")
                .email("email@email.com")
                .build();
        given(findUserPort.findByUserId(userId))
                .willReturn(Optional.of(user));

        boolean loginResult = loginUseCase.login(userId, password);

        assertThat(loginResult).isTrue();
        verify(findUserPort).findByUserId(userId);
    }

    @DisplayName("존재하지 않는 아이디로 로그인을 할 수 없다.")
    @Test
    void checkLoginFailedWithInvalidUserId() {
        String invalidUserId = "wrong_userId";
        String password = "password";
        given(findUserPort.findByUserId(invalidUserId))
                .willReturn(Optional.empty());

        assertThrows(NonExistsUserIdException.class, () -> loginUseCase.login(invalidUserId, password));
        verify(findUserPort).findByUserId(invalidUserId);
    }

    @DisplayName("일치하지 않는 패스워드로 로그인을 할 수 없다.")
    @Test
    void checkLoginFailedWithWrongPassword() {
        String userId = "userId";
        String realPassword = "password";
        String wrongPassword = "wrong_password";
        User user = User.builder()
                .userId(userId)
                .password(realPassword)
                .name("name")
                .email("email@email.com")
                .build();
        given(findUserPort.findByUserId(userId))
                .willReturn(Optional.of(user));

        boolean loginResult = loginUseCase.login(userId, wrongPassword);

        assertThat(loginResult).isFalse();
        verify(findUserPort).findByUserId(userId);
    }
}