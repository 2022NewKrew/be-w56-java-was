package http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpStatusTest {

    @DisplayName("상태 메시지 출력")
    @Test
    void toString_message() {
        assertThat(HttpStatus.OK.toString()).hasToString("200 OK");
        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.toString())
            .hasToString("500 Internal Server Error");
    }
}