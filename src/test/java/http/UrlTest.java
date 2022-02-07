package http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UrlTest {

    @Test
    void equals() {
        Url url1 = new Url(HttpMethod.GET, "/user");
        Url url2 = new Url(HttpMethod.GET, "/user?id=123");
        assertThat(url1).isEqualTo(url2);
    }
}