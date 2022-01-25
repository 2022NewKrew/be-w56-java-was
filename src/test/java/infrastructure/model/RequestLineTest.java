package infrastructure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RequestLineTest {

    @DisplayName("Request 객체를 정상적으로 생성한다.")
    @Test
    void checkCreateObject() {
        List<RequestLine> httpRequests = List.of(
                RequestLine.create("GET", "/index.html"),
                RequestLine.create("GET", "/favicon.ico"),
                RequestLine.create("GET", "/css/bootstrap.min.css"),
                RequestLine.create("GET", "/js/scripts.js")
        );

        assertThat(httpRequests)
                .extracting("requestMethod", "path.value", "path.contentType")
                .containsExactly(
                        tuple(RequestMethod.GET, "/index.html", ContentType.HTML),
                        tuple(RequestMethod.GET, "/favicon.ico", ContentType.ICO),
                        tuple(RequestMethod.GET, "/css/bootstrap.min.css", ContentType.CSS),
                        tuple(RequestMethod.GET, "/js/scripts.js", ContentType.JS)
                );
    }

    @DisplayName("지원하지 않는 요청 형식으로 객체를 생성할 수 없다.")
    @ParameterizedTest
    @CsvSource({"GETT,/index.html", "HEAD,/index.html"})
    void failCreateObject(String method, String uri) {
        assertThatThrownBy(() -> RequestLine.create(method, uri))
                .isInstanceOf(IllegalArgumentException.class);
    }
}