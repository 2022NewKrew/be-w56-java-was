package http;

import org.junit.jupiter.api.Test;

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
}
