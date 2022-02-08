package http;

import http.header.Cookie;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieTest {

    @Test
    void createCookieHeader() {
        Cookie cookie = new Cookie("A", "B");
        assertThat(cookie.createHeader()).isEqualTo("A=B; C=D");
    }

    @Disabled
    @Test
    public void parseCookies() {
//        String cookies = "logined=true; JSessionId=1234";
//        Cookie cookie = Cookie.parse(cookies);
//        assertThat(cookie.getCookie().get("logined")).isEqualTo("true");
//        assertThat(cookie.getCookie().get("JSessionId")).isEqualTo("1234");
    }

    @Disabled
    @Test
    public void setCookie() {
//        String cookies = "logined=true";
//        Cookie cookie = Cookie.parse(cookies);
//
//        cookie.setCookie("logined", "false");
//        assertThat(cookie.getCookie().get("logined")).isEqualTo("false");
//
//        cookie.setCookie("JSessionId", "1234");
//        assertThat(cookie.getCookie().get("JSessionId")).isEqualTo("1234");
    }
}
