package webserver.http.request;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import util.HttpRequestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class HttpRequestLineTest {
    @ParameterizedTest(name = "create(HttpRequestLine): {arguments}")
    @ValueSource(strings = {"GET /index.html HTTP/1.1", "POST /index.html HTTP/1.1", "GET /user/list.html HTTP/1.1"})
    public void create(String requestLine) {
        Map<String, String> map = HttpRequestUtils.parseRequest(requestLine);
        assertThat(HttpRequestLine.of(map)).isEqualTo((HttpRequestLine.of(map)));
    }

    @ParameterizedTest(name = "create failed by null: {arguments}")
    @NullSource
    public void createFailedFromNull(Map requestLine) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> HttpRequestLine.of(requestLine))
                .withMessageContaining("invalid input");
    }




}