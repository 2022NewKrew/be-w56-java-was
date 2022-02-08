package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import web.controller.RequestController;
import web.http.request.HttpRequest;
import web.http.response.HttpResponse;
import web.http.response.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("뷰 관련 테스트")
public class ViewServiceTest {

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
        httpRequest.getHeadersList().forEach(header ->
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

    @Test
    @DisplayName("로그인 된 유저라면 로그인 url 접근 시 index 페이지로 이동한다.")
    public void HTTP_요청_확인_테스트() throws IOException {
        // given
        String request = "GET /user/login.html HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cookie: logined=true; Path=/\r\n" +
                "Accept: */*\r\n\r\n";
        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);
        HttpRequest httpRequest = new HttpRequest(br);

        // when
        HttpResponse httpResponse = RequestController.getResponse(httpRequest);

        // then
        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.REDIRECT);
        assertThat(httpResponse.getHeaders().getHeaderByKey("Location"))
                .isEqualTo("Location: http://localhost:8080/index.html");
    }

    @ParameterizedTest
    @DisplayName("로그인 정보에 따라 유저 목록 페이지 접근이 다르다.")
    @MethodSource("getLoginInfo")
    public void 로그인_정보에_따른_유저목록페이지_접근테스트(LoginInfo loginInfo) throws IOException {
        // given
        String request = loginInfo.getLoginRequest();
        HttpStatus status = loginInfo.getStatus();
        String redirectUrl = loginInfo.getRedirectUrl();

        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);
        HttpRequest httpRequest = new HttpRequest(br);

        // when
        HttpResponse httpResponse = RequestController.getResponse(httpRequest);

        // then
        assertThat(httpResponse.getStatus()).isEqualTo(status);
        if(status.equals(HttpStatus.REDIRECT)){
            assertThat(httpResponse.getHeaders().getHeaderByKey("Location"))
                    .isEqualTo("Location: http://localhost:8080" + redirectUrl);
        }
    }

    private static Stream<LoginInfo> getLoginInfo(){
        return Stream.of(
                new LoginInfo("GET /user/list.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n\r\n", HttpStatus.REDIRECT,"/user/login.html"),
                new LoginInfo("GET /user/list.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Cookie: logined=true; Path=/\r\n" +
                        "Accept: */*\r\n\r\n", HttpStatus.OK, "/user/list.html"),
                new LoginInfo("GET /user/list.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Cookie: logined=false; Path=/\r\n" +
                        "Accept: */*\r\n\r\n", HttpStatus.REDIRECT,"/user/login.html")
        );
    }

    private static class LoginInfo{
        private final String loginRequest;
        private final HttpStatus status;
        private final String redirectUrl;

        public LoginInfo(String loginRequest, HttpStatus status, String redirectUrl) {
            this.loginRequest = loginRequest;
            this.status = status;
            this.redirectUrl = redirectUrl;
        }

        public String getLoginRequest() {
            return loginRequest;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }
    }
}
