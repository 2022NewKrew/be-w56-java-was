package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentTypeTest {
    @DisplayName("url에서 contentType 구하기 (default case)")
    @Test
    void getTypeFromUrlDefaultTest() {
        String url = "/user/create";
        String url2 = "/foo.unknown";

        Assertions.assertEquals(ContentType.getTypeFromUrl(url), ContentType.DEFAULT);
        Assertions.assertEquals(ContentType.getTypeFromUrl(url2), ContentType.DEFAULT);
    }

    @DisplayName("url에서 contentType 구하기 (CSS)")
    @Test
    void getTypeFromUrlCSSTest() {
        String url = "/foo.css";
        String url2 = "/foo/bar.css";

        Assertions.assertEquals(ContentType.getTypeFromUrl(url), ContentType.CSS);
        Assertions.assertEquals(ContentType.getTypeFromUrl(url2), ContentType.CSS);
    }
}