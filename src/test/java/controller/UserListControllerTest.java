package controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import db.DataBase;
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

@DisplayName("UserListController 테스트")
class UserListControllerTest {

    @DisplayName("올바른 파라미터를 받았을 때 올바른 HttpResponse 를 반환한다.")
    @Test
    void run() throws IOException {
        //give
        DataBase.addUser(new User("userId1", "password", "name1", "email1"));
        DataBase.addUser(new User("userId2", "password", "name2", "email2"));
        DataBase.addUser(new User("userId3", "password", "name3", "email3"));


        String startLineString = "GET /user/list.html HTTP/1.1\r\n";
        String headerString = "Cookie: logined=true\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "";

        RequestStartLine startLine = RequestStartLine.stringToRequestLine(startLineString);
        RequestHeader header = RequestHeader.stringToRequestHeader(headerString);
        RequestBody body = RequestBody.stringToRequestBody(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = UserListController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("200", "OK", "HTTP/1.1", "Content-Type",
                "Content-Length", "userId1", "name2", "email3");

        DataBase.deleteUser("userId1");
        DataBase.deleteUser("userId2");
        DataBase.deleteUser("userId3");
    }
}
