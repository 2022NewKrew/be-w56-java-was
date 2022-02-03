package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieTest {

    @Test
    void createCookieHeader() {
        Cookie cookie = new Cookie();
        cookie.setCookie("A", "B");
        cookie.setCookie("C", "D");

        assertThat(cookie.createCookieHeader()).isEqualTo("A=B; C=D;");
    }
}
