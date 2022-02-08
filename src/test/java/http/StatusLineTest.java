package http;

import http.startline.HttpStatus;
import http.startline.StatusLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StatusLineTest {
    @Test
    void createStatusLineFailed() {
        assertThatThrownBy(() -> StatusLine.create(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createStatusLineSuccess() {
        StatusLine.create(HttpStatus.OK);
    }

    @Test
    void equalStatusLine() {
        StatusLine test1 = StatusLine.create(HttpStatus.OK);
        StatusLine test2 = StatusLine.create(HttpStatus.NOT_FOUND);
        StatusLine test3 = StatusLine.create(HttpStatus.OK);

        assertThat(test1).isEqualTo(test3);
        assertThat(test1).isNotEqualTo(test2);
    }
}
