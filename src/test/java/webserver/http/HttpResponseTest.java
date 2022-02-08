package webserver.http;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseTest {

    private static final int SECONDS_IN_DAY = 86400;

    @Test
    void testHttpResponse() {
        // given
        HttpHeader expectedHeaders = new HttpHeader();
        String key = "test";
        String value = "true";
        expectedHeaders.put(key, value);

        HttpCookie expectedCookies = new HttpCookie();
        Cookie expectedCookie = new Cookie("myCookie", "yummy");
        expectedCookies.putCookie(expectedCookie);

        // when
        HttpResponse httpResponse = HttpResponse.builder()
                .status(HttpStatus.OK)
                .header(key, value)
                .cookie(expectedCookie)
                .build();

        // then
        assertThat(httpResponse).extracting(
                        HttpResponse::getStatus,
                        HttpResponse::getHeaders,
                        HttpResponse::getCookies)
                .containsExactly(
                        HttpStatus.OK,
                        expectedHeaders,
                        expectedCookies);
    }

    @Test
    void testHttpResponseWithMultipleHeaders() {
        // given
        HttpHeader expectedHeaders = new HttpHeader();
        expectedHeaders.put("key1", "value11");
        expectedHeaders.put("key1", "value12");
        expectedHeaders.put("key2", "value21");
        expectedHeaders.put("key3", "value31");

        // when
        HttpResponse httpResponse = HttpResponse.builder()
                .status(HttpStatus.OK)
                .header("key1", "value11")
                .header("key1", "value12")
                .header("key2", "value21")
                .header("key3", "value31")
                .build();
        List<String> resultKey1 = httpResponse.getHeaders().getValues("key1");

        // then
        assertThat(httpResponse).extracting(
                        HttpResponse::getStatus,
                        HttpResponse::getHeaders)
                .containsExactly(
                        HttpStatus.OK,
                        expectedHeaders);
        assertThat(resultKey1.size()).isEqualTo(2);
        assertThat(resultKey1).containsExactly("value11", "value12");
    }

    @Test
    void testHttpResponseWithMultipleCookies() {
        // given
        HttpCookie expectedCookies = new HttpCookie();

        Cookie cookie1 = new Cookie("myCookie", "yummy");
        cookie1.setMaxAge(SECONDS_IN_DAY);
        cookie1.setPath("/");

        Cookie cookie2 = new Cookie("myCookie2", "yummy2");
        cookie2.setDomain("foo.bar");
        cookie2.setSecure(true);
        cookie2.setHttpOnly(true);

        expectedCookies.putCookie(cookie1);
        expectedCookies.putCookie(cookie2);

        // when
        HttpResponse httpResponse = HttpResponse.builder()
                .status(HttpStatus.OK)
                .cookie(cookie1)
                .cookie(cookie2)
                .build();

        // then
        assertThat(httpResponse).extracting(
                        HttpResponse::getStatus,
                        HttpResponse::getCookies)
                .containsExactly(
                        HttpStatus.OK,
                        expectedCookies);
    }
}
