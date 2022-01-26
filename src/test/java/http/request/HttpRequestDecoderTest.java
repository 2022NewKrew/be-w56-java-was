package http.request;

import http.header.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class HttpRequestDecoderTest {

    @DisplayName("HttpRequest 디코딩")
    @ParameterizedTest
    @MethodSource("provideHttpRequestString")
    void decode(String httpRequestString, HttpRequest expected) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(httpRequestString));

        HttpRequest parsed = HttpRequestDecoder.decode(br);
        assertThat(parsed)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private static Stream<Arguments> provideHttpRequestString() {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Host", "localhost:8080");
        headers1.add("Accept", "text/html,application/xhtml+xml;q=0.8");

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Host", "123.123.123.123");
        headers2.add("Content-Length", "5");

        return Stream.of(
                Arguments.of(
                        "GET / HTTP/1.1\r\n"
                        + "Host: localhost:8080\r\n"
                        + "Accept: text/html,application/xhtml+xml;q=0.8\r\n",
                        HttpRequest.builder()
                                .method(HttpRequestMethod.GET)
                                .uri("/")
                                .protocolVersion("HTTP/1.1")
                                .headers(headers1)
                                .build()
                ),
                Arguments.of(
                        "POST /abc HTTP/1.1\r\n"
                        + "Host: 123.123.123.123\r\n"
                        + "Content-Length: 5\r\n"
                        + "\r\n"
                        + "abcde",
                        HttpRequest.builder()
                                .method(HttpRequestMethod.POST)
                                .uri("/abc")
                                .protocolVersion("HTTP/1.1")
                                .headers(headers2)
                                .body("abcde")
                                .build()
                )
        );
    }
}
