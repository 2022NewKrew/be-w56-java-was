package application.in.user;

import application.exception.user.AlreadyExistingUserException;
import application.out.user.FindUserPort;
import application.out.user.SignUpUserPort;
import application.user.SignUpUserService;
import domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class SignUpUserUseCaseTest {

    SignUpUserUseCase signUpUserUseCase;

    @Mock
    SignUpUserPort signUpUserPort;

    @Mock
    FindUserPort findUserPort;

    @BeforeEach
    void injectMock() {
        signUpUserUseCase = new SignUpUserService(signUpUserPort, findUserPort);
    }

    @AfterEach
    void checkAnotherCall() { verifyNoMoreInteractions(signUpUserPort); }

    @DisplayName("사용자는 회원 정보를 입력해서 회원가입 할 수 있다.")
    @Test
    void checkSignUp() {
        String userId = "userId";
        User user = User.builder()
                .userId(userId)
                .password("password")
                .name("name")
                .email("email@email.com")
                .build();
        given(findUserPort.findByUserId(userId))
                .willReturn(Optional.empty());

        signUpUserUseCase.signUp(user);

        verify(findUserPort).findByUserId(userId);
        verify(signUpUserPort).addUser(user);
    }

    @DisplayName("사용자는 이미 존재하는 아이디로 회원가입 할 수 없다.")
    @Test
    void checkSignUpWithDuplicatedUserId() {
        String userId = "userId";
        User user = User.builder()
                .userId(userId)
                .password("password")
                .name("name")
                .email("email@email.com")
                .build();
        given(findUserPort.findByUserId(userId))
                .willReturn(Optional.of(user));

        assertThrows(AlreadyExistingUserException.class, () -> signUpUserUseCase.signUp(user));

        verify(findUserPort).findByUserId(userId);
        verify(signUpUserPort, never()).addUser(user);
    }
}