package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResponseMessageTest {

    @Test
    void createFailed_WhenNull() {
        assertThatThrownBy(() -> ResponseMessage.create(HttpStatus.OK, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ResponseMessage.create(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ResponseMessage.create(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSuccess() {
        ResponseMessage.create(HttpStatus.OK, new byte[]{});
        ResponseMessage.create(HttpStatus.FOUND, "/", new Cookie());
    }
}
