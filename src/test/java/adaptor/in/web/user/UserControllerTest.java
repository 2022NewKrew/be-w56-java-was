package adaptor.in.web.user;

import application.exception.user.AlreadyExistingUserException;
import application.exception.user.NonExistsUserIdException;
import application.in.session.SetSessionUseCase;
import application.in.user.FindUserUseCase;
import application.in.user.LoginUseCase;
import application.in.user.SignUpUserUseCase;
import domain.user.User;
import infrastructure.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    UserController userController;

    @Mock
    SetSessionUseCase setSessionUseCase;

    @Mock
    SignUpUserUseCase signUpUserUseCase;

    @Mock
    LoginUseCase loginUseCase;

    @Mock
    FindUserUseCase findUserUseCase;

    @BeforeEach
    void injectMock() {
        userController = new UserController(setSessionUseCase, signUpUserUseCase, loginUseCase, findUserUseCase);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(setSessionUseCase);
        verifyNoMoreInteractions(signUpUserUseCase);
        verifyNoMoreInteractions(loginUseCase);
        verifyNoMoreInteractions(findUserUseCase);
    }

    @DisplayName("사용자는 회원 정보로 회원가입할 수 있다.")
    @Test
    void checkSignUp() {
        User user = User.builder().userId("id").password("pwd").name("test").email("qwerty@kakao.com").build();
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.POST, new Path(ContentType.UNKNOWN, "/user/create")),
                HttpHeader.of(),
                new HttpStringBody("userId=id&password=pwd&name=test&email=qwerty@kakao.com")
        );

        HttpResponse response = userController.handle(httpRequest);

        verify(signUpUserUseCase).signUp(user);
        assertThat(response.getResponseLine().getHttpStatus())
                .isEqualTo(HttpStatus.FOUND);
    }

    @DisplayName("이미 존재하는 회원 정보로 회원가입할 수 없다.")
    @Test
    void checkSignUpWithDuplicatedInfo() {
        User user = User.builder().userId("id").password("pwd").name("test").email("qwerty@kakao.com").build();
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.POST, new Path(ContentType.UNKNOWN, "/user/create")),
                HttpHeader.of(),
                new HttpStringBody("userId=id&password=pwd&name=test&email=qwerty@kakao.com")
        );
        willThrow(new AlreadyExistingUserException())
                .given(signUpUserUseCase)
                .signUp(user);

        HttpResponse response = userController.handle(httpRequest);

        verify(signUpUserUseCase).signUp(user);
        assertThat(response.getResponseLine().getHttpStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("아이디와 비밀번호로 로그인할 수 있다.")
    @Test
    void checkLogin() {
        String userId = "id";
        String password = "pwd";
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.POST, new Path(ContentType.UNKNOWN, "/user/login")),
                HttpHeader.of(),
                new HttpStringBody("userId=id&password=pwd")
        );
        given(loginUseCase.login(userId, password))
                .willReturn(true);

        HttpResponse response = userController.handle(httpRequest);

        verify(loginUseCase).login(userId, password);
        verify(setSessionUseCase).setSession("loginId", userId);
        assertThat(response.getResponseLine().getHttpStatus())
                .isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHttpHeader().getHeader("Set-Cookie"))
                .isNotNull();
    }

    @DisplayName("잘못된 비밀번호로 로그인할 수 없다.")
    @Test
    void checkLoginFailWithWrongPassword() {
        String userId = "id";
        String password = "wrong";
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.POST, new Path(ContentType.UNKNOWN, "/user/login")),
                HttpHeader.of(),
                new HttpStringBody("userId=id&password=wrong")
        );
        given(loginUseCase.login(userId, password))
                .willReturn(false);

        HttpResponse response = userController.handle(httpRequest);

        verify(loginUseCase).login(userId, password);
        assertThat(response.getResponseLine().getHttpStatus())
                .isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHttpHeader().getHeader("Set-Cookie"))
                .isNull();
    }

    @DisplayName("존재하지 않는 계정으로 로그인할 수 없다.")
    @Test
    void checkLoginFailWithNotExistingAccount() {
        String userId = "wrong";
        String password = "pwd";
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.POST, new Path(ContentType.UNKNOWN, "/user/login")),
                HttpHeader.of(),
                new HttpStringBody("userId=wrong&password=pwd")
        );
        willThrow(new NonExistsUserIdException())
                .given(loginUseCase)
                .login(userId, password);

        HttpResponse response = userController.handle(httpRequest);

        verify(loginUseCase).login(userId, password);
        assertThat(response.getResponseLine().getHttpStatus())
                .isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHttpHeader().getHeader("Set-Cookie"))
                .isNull();
    }

    @DisplayName("로그인 한 사용자는 모든 사용자 목록을 볼 수 있다.")
    @Test
    void checkAllUserList() {
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.GET, new Path(ContentType.UNKNOWN, "/user/list")),
                HttpHeader.of(new Pair("Cookie", "SESSION_ID=45")),
                new HttpStringBody("userId=id&password=pwd")
        );
        List<User> users = List.of(
                new User("id1", "pwd1", "name", "email@naver.com"),
                new User("userId", "password", "kakaoKim", "asdf@google.com")
        );
        given(findUserUseCase.findAll())
                .willReturn(users);

        HttpResponse response = userController.handle(httpRequest);

        verify(findUserUseCase).findAll();
        assertThat(response.getResponseLine().getHttpStatus())
                .isEqualTo(HttpStatus.OK);
    }
}