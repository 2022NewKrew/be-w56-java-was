package service;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.HttpRequestUtils;
import web.controller.RequestController;
import web.http.request.HttpRequest;
import web.http.response.HttpResponse;
import web.http.response.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("유저 관련 테스트")
class UserServiceTest {

    @BeforeAll
    public static void beforeAll(){
        dummyUsers().forEach(DataBase::addUser);
    }

    private static List<User> dummyUsers(){
        return List.of(
                new User("testUser", "12341234", "test", "test@test.com"),
                new User("testUser2", "12341234", "test2", "test2@test.com")
        );
    }

    @ParameterizedTest
    @MethodSource("getSignUpHttpRequestString")
    @DisplayName("입력받은 정보로 회원가입이 가능해야 한다.")
    public void 유저_회원가입_테스트(String request) throws IOException {
        // given
        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);
        HttpRequest httpRequest = new HttpRequest(br);
        Map<String, String> userInfo = HttpRequestUtils.parseBody(httpRequest.getBodyData());
        String userId = userInfo.get("userId");

        // when
        HttpResponse httpResponse = RequestController.getResponse(httpRequest);

        // then
        assertThat(DataBase.findUserById(userId)).isInstanceOf(User.class);
        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.REDIRECT);
        assertThat(httpResponse.getHeaders().getHeaderByKey("Location"))
                .isEqualTo("Location: http://localhost:8080/index.html");
    }

    private static Stream<String> getSignUpHttpRequestString() {
        return Stream.of(
                "POST /user/create HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 72\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=grimm&password=12341234&name=name&email=grimm.lee@kakaocorp.com\r\n",
                "POST /user/create HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 72\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=grimm2&password=1234&name=name2&email=grimm2.lee@kakaocorp.com\r\n"
        );
    }

    @ParameterizedTest
    @MethodSource("getOKLoginHttpRequestString")
    @DisplayName("제대로된 아이디와 비밀번호를 입력했으면 로그인 되어야 한다.")
    public void 아이디_비밀번호_정상입력시_로그인(String request) throws IOException {
        // given
        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);
        HttpRequest httpRequest = new HttpRequest(br);

        // when
        HttpResponse httpResponse = RequestController.getResponse(httpRequest);

        // then
        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.REDIRECT);
        assertThat(httpResponse.getHeaders().getHeaderByKey("Set-Cookie"))
                .isEqualTo("Set-Cookie: logined=true; Path=/");
        assertThat(httpResponse.getHeaders().getHeaderByKey("Location"))
                .isEqualTo("Location: http://localhost:8080/index.html");
    }

    @ParameterizedTest
    @MethodSource("getFailLoginHttpRequestString")
    @DisplayName("아이디와 비밀번호 잘못 입력시 로그인 실패 페이지로 이동한다.")
    public void 아이디_비밀번호_잘못입력시_로그인실패(String request) throws IOException {
        // given
        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);
        HttpRequest httpRequest = new HttpRequest(br);

        // when
        HttpResponse httpResponse = RequestController.getResponse(httpRequest);

        // then
        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.REDIRECT);
        assertThat(httpResponse.getHeaders().getHeaderByKey("Set-Cookie"))
                .isEqualTo("Set-Cookie: logined=false; Path=/");
        assertThat(httpResponse.getHeaders().getHeaderByKey("Location"))
                .isEqualTo("Location: http://localhost:8080/user/login_failed.html");
    }

    private static Stream<String> getOKLoginHttpRequestString() {
        return Stream.of(
                "POST /user/login HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 33\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=testUser&password=12341234\r\n",
                "POST /user/login HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 34\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=testUser2&password=12341234\r\n"
        );
    }

    private static Stream<String> getFailLoginHttpRequestString() {
        return Stream.of(
                "POST /user/login HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 33\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=testUser&password=1234\r\n",
                "POST /user/login HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 34\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=testUser2&password=1234\r\n"
        );
    }
}
