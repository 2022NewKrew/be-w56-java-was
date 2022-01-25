package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestMessageTest {

    @Test
    void createRequestMessageFailed_WhenNull() {
        assertThatThrownBy(() -> new RequestMessage(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
