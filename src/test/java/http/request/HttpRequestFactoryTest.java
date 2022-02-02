package http.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import webserver.HttpMethod;

@DisplayName("HttpRequestFactory 테스트")
class HttpRequestFactoryTest {

    @DisplayName("getHttpRequest 메서드를 올바른 파라메터로 실행 했을 때 HttpRequest 객체를 생성한다.")
    @Test
    void getHttpRequest()
            throws IOException {
        //give
        String testRequest = "POST /test1 HTTP/1.1\r\n"
                + "Content-Length: 39\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n"
                + "bodyKey1=bodyValue1&bodyKey2=bodyValue2";
        InputStream testInputStream = new ByteArrayInputStream(testRequest.getBytes());
        //when
        HttpRequest request = HttpRequestFactory.getHttpRequest(testInputStream);
        //then
        assertThat(request.getUrl()).isEqualTo("/test1");
        assertThat(request.getBodyData().get("bodyKey1")).isEqualTo("bodyValue1");
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    }

    @DisplayName("getHttpRequest 메서드를 올바른 파라메터로(query 포함) 실행 했을 때 HttpRequest 객체를 생성한다.")
    @Test
    void getHttpRequestWithQuery()
            throws IOException {
        //give
        String testRequest = "POST /test1?queryKey1=queryValue1 HTTP/1.1\r\n"
                + "Content-Length: 39\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n"
                + "bodyKey1=bodyValue1&bodyKey2=bodyValue2";
        InputStream testInputStream = new ByteArrayInputStream(testRequest.getBytes());
        //when
        HttpRequest request = HttpRequestFactory.getHttpRequest(testInputStream);
        //then
        assertThat(request.getUrl()).isEqualTo("/test1");
        assertThat(request.getBodyData().get("bodyKey1")).isEqualTo("bodyValue1");
        assertThat(request.getQuery().get("queryKey1")).isEqualTo("queryValue1");
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    }
}
