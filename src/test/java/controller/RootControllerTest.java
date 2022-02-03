package controller;

import static org.assertj.core.api.Assertions.assertThat;

import http.request.HttpRequest;
import http.request.RequestBody;
import http.request.RequestHeader;
import http.request.RequestStartLine;
import http.response.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RootController 테스트")
class RootControllerTest {

    @DisplayName("루트로 GET 요청이 들어오면 /index.html 로 리다이렉트 한다.")
    @Test
    void run() throws IOException {
        //give
        String startLineString = "GET / HTTP/1.1\r\n";
        String headerString = "headerKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "";

        RequestStartLine startLine = RequestStartLine.stringToRequestLine(startLineString);
        RequestHeader header = RequestHeader.stringToRequestHeader(headerString);
        RequestBody body = RequestBody.stringToRequestBody(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = RootController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("302", "Found", "HTTP/1.1", "Location",
                "/index.html");
    }
}
