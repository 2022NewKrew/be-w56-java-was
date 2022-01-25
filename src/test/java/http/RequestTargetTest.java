package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestTargetTest {

    @Test
    void createRequestTargetFailedWhenEmpty() {
        assertThatThrownBy(() -> new RequestTarget(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRequestTargetFailedWhenNull() {
        assertThatThrownBy(() -> new RequestTarget(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRequestTargetSuccess() {
        new RequestTarget("abcd");
    }

    @Test
    void findPath_WhenDirectory() {
        assertThat(new RequestTarget("/").findPath())
                .isEqualTo("./webapp/index.html");
    }

    @Test
    void findPathSuccess() {
        assertThat(new RequestTarget("/abc").findPath())
                .isEqualTo("./webapp/abc");
    }
}
