package util;

import static org.assertj.core.api.Assertions.*;

import http.HttpMethod;
import http.request.HttpRequest;
import http.request.RequestBody;
import http.request.RequestLine;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class IOUtilsTest {

    private static final String httpRequest = "POST /users/create HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Content-Length: 59\n"
            + "Content-Type: application/x-www-form-urlencoded\n"
            + "Accept: */*\n"
            + "\n"
            + "userId=javajigi&password=password&name=javajigi&email=javajigi@slipp.net";

    @Test
    public void readRequest() throws IOException {
        InputStream in = new ByteArrayInputStream(httpRequest.getBytes());
        HttpRequest httpRequest = IOUtils.readRequest(in);
        RequestLine requestLine = httpRequest.getRequestLine();
        RequestBody requestBody = httpRequest.getBody();

        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(requestLine.getUri().getPath()).isEqualTo("/users/create");
        assertThat(requestBody.getValue("userId")).isEqualTo("javajigi");
    }
}
