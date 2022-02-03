package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestLineTest {
    @DisplayName("HttpRequestLine 생성이 정상적인지 확인")
    @Test
    void HttpRequestLineCreate(){
        String line = "GET /users/login?aaa=aaa&bbb=bbb&ccc HTTP/1.1";

        try {
            HttpRequestLine httpRequestLine = HttpRequestLine.makeHttpRequestLine(line);

            assertThat(httpRequestLine.getRequestUrl()).isEqualTo("/users/login?aaa=aaa&bbb=bbb&ccc");
            assertThat(httpRequestLine.getUrl()).isEqualTo("/users/login");
            assertThat(httpRequestLine.getMethod()).isEqualTo("GET");
        } catch (IOException e) {}


        //잘못된 생성 검증
        assertThrows(IOException.class, () -> HttpRequestLine.makeHttpRequestLine("Get"));
        assertThrows(IOException.class, () -> HttpRequestLine.makeHttpRequestLine("Get / "));
        assertThrows(IOException.class, () -> HttpRequestLine.makeHttpRequestLine(""));
    }
}
