package controller;

import http.request.HttpRequest;
import http.request.HttpRequestLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerTest {
    @DisplayName("정적 파일에 대한 요청 확인")
    @Test
    void isStaticFileSRequestTest() {
        String requestLineStr = "GET /index.html HTTP/1.1";
        HttpRequestLine requestLine = HttpRequestLine.parseRequestLine(requestLineStr);
        HttpRequest request = new HttpRequest(requestLine, null, null);
        Controller controller = UserController.getInstance();

        Assertions.assertTrue(controller.isStaticFileRequest(request));
    }

    @DisplayName("정적 파일에 대한 요청이 아닐 때 확인")
    @Test
    void isStaticFileSRequestFalseTest() {
        String requestLineStr = "GET /user/create HTTP/1.1";
        HttpRequestLine requestLine = HttpRequestLine.parseRequestLine(requestLineStr);
        HttpRequest request = new HttpRequest(requestLine, null, null);
        Controller controller = UserController.getInstance();

        Assertions.assertFalse(controller.isStaticFileRequest(request));
    }

}