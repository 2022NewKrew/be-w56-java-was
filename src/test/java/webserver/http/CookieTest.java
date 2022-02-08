package webserver.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieTest {

    private static final int SECONDS_IN_DAY = 86400;

    @Test
    void testDefaultCookie() {
        String expectedCookie = "name=value; Max-Age=-1";

        Cookie cookie = new Cookie("name", "value");
        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

    @Test
    void testCookieAttributeDomain() {
        String expectedCookie = "name=value; Max-Age=-1; Domain=foo.bar";

        Cookie cookie = new Cookie("name", "value");
        cookie.setDomain("foo.bar");
        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

    @Test
    void testCookieAttributeMaxAge() {
        String expectedCookie = "name=value; Max-Age=" + SECONDS_IN_DAY;

        Cookie cookie = new Cookie("name", "value");
        cookie.setMaxAge(SECONDS_IN_DAY);
        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

    @Test
    void testCookieAttributePath() {
        String expectedCookie = "name=value; Max-Age=-1; Path=/";

        Cookie cookie = new Cookie("name", "value");
        cookie.setPath("/");
        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

    @Test
    void testCookieAttributeSecure() {
        String expectedCookie = "name=value; Max-Age=-1; Secure";

        Cookie cookie = new Cookie("name", "value");
        cookie.setSecure(true);
        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

    @Test
    void testCookieAttributeHttpOnly() {
        String expectedCookie = "name=value; Max-Age=-1; HttpOnly";

        Cookie cookie = new Cookie("name", "value");
        cookie.setHttpOnly(true);
        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

    @Test
    void testCookieMultipleAttributes() {
        String expectedCookie = "name=value; Max-Age=86400; Domain=foo.bar; Path=/some/path; Secure; HttpOnly";

        Cookie cookie = new Cookie("name", "value");
        cookie.setDomain("foo.bar");
        cookie.setMaxAge(SECONDS_IN_DAY);
        cookie.setPath("/some/path");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        assertThat(cookie.toString()).isEqualTo(expectedCookie);
    }

}
