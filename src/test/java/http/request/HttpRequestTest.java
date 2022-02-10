package http.request;

import dao.UserDBConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class HttpRequestTest {
    private static final String TEST_RESOURCES_PATH = "./src/test/resources";

    @DisplayName("HTTP request 읽어오기 테스트 - GET")
    @Test
    void readWithBufferedReaderGetTest() throws IOException {
        InputStream in = new FileInputStream(new File(TEST_RESOURCES_PATH, "request_get.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.readWithBufferedReader(br);

        Assertions.assertEquals("GET", request.line().method());
        Assertions.assertEquals("/index.html", request.line().path());
        Assertions.assertEquals("keep-alive", request.header().getIfPresent("Connection"));
    }

    @DisplayName("HTTP request 읽어오기 테스트 - POST")
    @Test
    void readWithBufferedReaderPostTest() throws IOException {
        InputStream in = new FileInputStream(new File(TEST_RESOURCES_PATH, "request_post.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.readWithBufferedReader(br);
        Map<String, String> query = HttpRequestUtils.parseQueryString(request.body().content());

        Assertions.assertEquals("POST", request.line().method());
        Assertions.assertEquals("/user/create", request.line().path());
        Assertions.assertEquals("aid", query.get(UserDBConstants.COLUMN_USER_ID));
        Assertions.assertEquals("a@a.com", query.get(UserDBConstants.COLUMN_EMAIL));
    }
}