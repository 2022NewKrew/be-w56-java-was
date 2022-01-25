package http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpMethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "get"})
    void matchValueFailedWhenInvalidMethod(String method) {
        assertThat(HttpMethod.matchValue(method)).isEqualTo(HttpMethod.NONE);
    }

    @Test
    void matchValueFailedWhenNull() {
        assertThat(HttpMethod.matchValue(null)).isEqualTo(HttpMethod.NONE);
    }

    @Test
    void matchValueSuccess() {
        assertThat(HttpMethod.matchValue("GET")).isEqualTo(HttpMethod.GET);
        assertThat(HttpMethod.matchValue("POST")).isEqualTo(HttpMethod.POST);
        assertThat(HttpMethod.matchValue("PUT")).isEqualTo(HttpMethod.PUT);
        assertThat(HttpMethod.matchValue("DELETE")).isEqualTo(HttpMethod.DELETE);
    }
}
