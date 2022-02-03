package http;

import static org.assertj.core.api.Assertions.*;

import http.request.Path;
import org.junit.jupiter.api.Test;

class MediaTypeTest {

    @Test
    void getMediaType() {
        Path path = new Path("/css/styles.css");

        MediaType result = MediaType.getMediaType(path);

        assertThat(result.getType()).isEqualTo("text/html");
    }

    @Test
    void getMediaType_default() {
        Path path = new Path("/users/create");

        MediaType result = MediaType.getMediaType(path);

        assertThat(result.getType()).isEqualTo("*/*");
    }
}
