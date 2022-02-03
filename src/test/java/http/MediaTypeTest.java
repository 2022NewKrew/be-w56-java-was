package http;

import static org.assertj.core.api.Assertions.*;

import http.request.Queries;
import http.request.URI;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class MediaTypeTest {

    @Test
    void getMediaType() {
        URI uri = new URI("/css/styles.css", new Queries(new HashMap<>()));

        MediaType result = MediaType.getMediaType(uri);

        assertThat(result.getType()).isEqualTo("text/css");
    }

    @Test
    void getMediaType_default() {
        URI uri = new URI("/users/create", new Queries(new HashMap<>()));

        MediaType result = MediaType.getMediaType(uri);

        assertThat(result.getType()).isEqualTo("*/*");
    }
}
