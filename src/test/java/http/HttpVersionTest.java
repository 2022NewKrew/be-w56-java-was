package http;

import http.startline.HttpVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpVersionTest {

    @ParameterizedTest
    @ValueSource(strings = {""})
    void matchValueFailedWhenInvalidVersion(String version) {
        assertThat(HttpVersion.matchValue(version)).isEqualTo(HttpVersion.NONE);
    }

    @Test
    void matchValueFailedWhenNull() {
        assertThat(HttpVersion.matchValue(null)).isEqualTo(HttpVersion.NONE);
    }

    @Test
    void matchValueSuccess() {
        assertThat(HttpVersion.matchValue("HTTP/1.0")).isEqualTo(HttpVersion.V_1_0);
        assertThat(HttpVersion.matchValue("HTTP/1.1")).isEqualTo(HttpVersion.V_1_1);
    }
}
