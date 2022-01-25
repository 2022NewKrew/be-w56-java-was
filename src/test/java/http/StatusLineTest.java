package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StatusLineTest {
    @Test
    void createStatusLineFailed() {
        assertThatThrownBy(() -> new StatusLine(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createStatusLineSuccess() {
        new StatusLine(HttpVersion.V_1_0, HttpStatus.OK);
    }

    @Test
    void equalStatusLine() {
        assertThat(new StatusLine(HttpVersion.V_1_0, HttpStatus.OK))
                .isEqualTo(new StatusLine(HttpVersion.V_1_0, HttpStatus.OK));

        assertThat(new StatusLine(HttpVersion.V_1_0, HttpStatus.OK))
                .isNotEqualTo(new StatusLine(HttpVersion.V_1_0, HttpStatus.NOT_FOUND));
    }
}
