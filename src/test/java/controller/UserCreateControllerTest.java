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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserCreateController 테스트")
class UserCreateControllerTest {

    @DisplayName("올바른 파라미터를 받았을 때 올바른 HttpResponse 를 반환한다.")
    @Test
    void run() throws IOException {
        //give
        String startLineString = "POST /index.html HTTP/1.1\r\n";
        String headerString = "Content-Length: 53\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "userId=userId&password=password&name=name&email=email";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserCreateController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("302", "Found", "HTTP/1.1", "Location");

        UserDao.getInstance().delete(new User("userId", "password", "name", "email"));
    }

    @DisplayName("HttpRequest 의 body 가 올바르지 못한 경우 BadRequestException 을 던진다.")
    @Test
    void runWithoutBody() {
        //give
        String startLineString = "POST /index.html HTTP/1.1\r\n";
        String headerString = "Content-Length: 53\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserCreateController.getInstance();
        //when
        //then
        assertThatThrownBy(() -> controller.run(request, dos)).isInstanceOf(
                BadRequestException.class);
    }
}
