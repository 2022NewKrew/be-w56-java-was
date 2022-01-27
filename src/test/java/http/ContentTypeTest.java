package http;

import static org.assertj.core.api.Assertions.*;

import exception.ContentTypeNotFoundException;
import org.junit.jupiter.api.Test;

class ContentTypeTest {

    @Test
    void match() {
        String path = "/index.html";

        ContentType result = ContentType.match(path);

        assertThat(result.getType()).isEqualTo("text/html");
    }

    @Test
    void match_notFoundException() {
        String path = "/index.ht";

        assertThatThrownBy(() -> ContentType.match(path)).isInstanceOf(ContentTypeNotFoundException.class);
    }
}
