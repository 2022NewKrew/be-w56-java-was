package controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import http.request.HttpRequest;
import http.request.RequestBody;
import http.request.RequestHeader;
import http.request.RequestStartLine;
import http.response.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

class ArticleCreateControllerTest {

    @Test
    void run() throws IOException {
        //give
        String startLineString = "POST /qna/create HTTP/1.1\r\n";
        String headerString = "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n";
        String bodyString = "content=testContent";

        RequestStartLine startLine = RequestStartLine.from(startLineString);
        RequestHeader header = RequestHeader.from(headerString);
        RequestBody body = RequestBody.from(bodyString);
        HttpRequest request = new HttpRequest(startLine, header, body);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        Controller controller = ArticleCreateController.getInstance();

        //when
        HttpResponse response = controller.run(request, dos);
        response.sendResponse();

        assertThat(outputStream.toString()).contains("302", "Found", "Location");
    }
}
