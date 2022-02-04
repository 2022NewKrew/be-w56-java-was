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

@DisplayName("StaticFileController 테스트")
class StaticFileControllerTest {

    @DisplayName("올바른 파라미터를 넘겨 받았을 때 올바른 HttpResponse 를 반환한다.")
    @Test
    void run() throws IOException {
        //give
        String startLineString = "GET /index.html HTTP/1.1\r\n";
        String headerString = "headerKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = StaticFileController.getInstance();
        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();
        //then
        assertThat(outputStream.toString()).contains("200", "OK", "HTTP/1.1", "Content-Type",
                "text/html", "Content-Length");
    }
}
