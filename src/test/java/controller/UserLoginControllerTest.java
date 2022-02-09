package controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dao.UserDao;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.request.RequestBody;
import http.request.RequestHeader;
import http.request.RequestStartLine;
import http.response.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserLoginController 테스트")
class UserLoginControllerTest {

    @BeforeAll
    private static void before() {
        UserDao dao = UserDao.getInstance();
        dao.save(new User("userId", "password", "name", "email"));
    }

    @AfterAll
    private static void after() {
        UserDao dao = UserDao.getInstance();
        UserDao.getInstance().delete(UserDao.getInstance().findByUserId("userId").getId());
    }

    @DisplayName("올바른 파라미터를 받았을 때 올바른 HttpResponse 를 반환한다.")
    @Test
    void run() throws IOException {
        //give
        String startLineString = "POST /index.html HTTP/1.1\r\n";
        String headerString = "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "userId=userId&password=password";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserLoginController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("302", "Found", "Location", "Set-Cookie");
    }

    @DisplayName("body 로 받은 userId 가 데이터베이스에 존재하지 않을 경우 401 과 함께 /user/login_fail.html 을 반환한다.")
    @Test
    void dataBaseDoesNotHaveUser() throws IOException {
        //give
        String startLineString = "POST /index.html HTTP/1.1\r\n";
        String headerString = "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "userId=otherUserId&password=password";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserLoginController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("401", "Unauthorized", "Content-Type",
                "Content-Length");
        assertThat(outputStream.toString()).doesNotContain("Set-Cookie");
    }

    @DisplayName("body 로 받은 password 가 일치하지 않을 경우 401 과 함께 /user/login_fail.html 을 반환한다.")
    @Test
    void wrongPassword() throws IOException {
        //give
        String startLineString = "POST /index.html HTTP/1.1\r\n";
        String headerString = "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "userId=userId&password=otherPassword";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserLoginController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("401", "Unauthorized", "Content-Type",
                "Content-Length");
        assertThat(outputStream.toString()).doesNotContain("Set-Cookie");
    }

    @DisplayName("HttpRequest 의 body 가 올바르지 못한 경우 BadRequestException 을 던진다.")
    @Test
    void runWithoutBody() {
        //give
        String startLineString = "POST /index.html HTTP/1.1\r\n";
        String headerString = "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserLoginController.getInstance();
        //when
        //then
        assertThatThrownBy(() -> controller.run(request, dos)).isInstanceOf(
                BadRequestException.class);
    }
}
