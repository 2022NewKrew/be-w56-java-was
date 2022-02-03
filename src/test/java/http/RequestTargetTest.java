package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestTargetTest {

    @Test
    void createRequestTargetFailedWhenEmpty() {
        String input = "";

        assertThatThrownBy(() -> RequestTarget.create(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRequestTargetFailedWhenNull() {
        assertThatThrownBy(() -> RequestTarget.create(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRequestTargetSuccessOnlyPath() {
        RequestTarget.create("/input.html");
    }

    @Test
    void createRequestTargetSuccessIncludeQuery() {
        RequestTarget.create("/user/create?userId=asdf&userName=userName");
    }
}
