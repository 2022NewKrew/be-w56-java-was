package service;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.HttpRequestUtils;
import web.http.request.HttpRequest;
import web.http.response.HttpResponse;
import web.http.response.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("유저 관련 테스트")
class UserServiceTest {

    @ParameterizedTest
    @DisplayName("HTTP GET 요청 테스트")
    @MethodSource("getHttpRequestString")
    public void HTTP_요청_확인_테스트(String request) throws IOException {
        // given
        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);
        List<String> requestStrings = List.of(request.split("\r\n"));

        // when
        HttpRequest httpRequest = new HttpRequest(br);

        // then
        assertThat(httpRequest.getHttpRequestLine().toString()).isEqualTo(requestStrings.get(0));

        AtomicInteger index = new AtomicInteger();
        httpRequest.getHttpRequestHeaders()
                .getHeaders().forEach(header ->
                        assertThat(header.toString()).isEqualTo(requestStrings.get(index.getAndIncrement()+1)));
    }

    private static Stream<String> getHttpRequestString() {
        return Stream.of(
                "GET /index.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n\r\n",
                "GET /user/login.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n\r\n",
                "GET /user/form.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n\r\n"
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
        HttpResponse httpResponse = UserService.signUp(httpRequest);

        // then
        assertThat(DataBase.findUserById(userId)).isInstanceOf(User.class);
        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.REDIRECT);
        assertThat(httpResponse.getHeaders().getHeaderByKey("Location"))
                .isEqualTo("Location: http://localhost:8080/index.html");
    }

    private static Stream<String> getSignUpHttpRequestString() {
        return Stream.of(
                "POST /user/login HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 72\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=grimm&password=12341234&name=name&email=grimm.lee@kakaocorp.com\r\n",
                "POST /user/login HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 72\r\n" +
                        "Accept: */*\r\n\r\n"+
                        "userId=grimm2&password=1234&name=name2&email=grimm2.lee@kakaocorp.com\r\n"
        );
    }

}
