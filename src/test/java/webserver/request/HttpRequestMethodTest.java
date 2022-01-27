package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class HttpRequestMethodTest {

    @Test
    @DisplayName("HttpMethod 형식이 아닐시 에러")
    void test1() {
        assertThatExceptionOfType(Exception.class).isThrownBy(
                () -> HttpRequestMethod.valueOf("asd")
        );
    }

}
