package webserver.http;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.http.Header.*;

class HttpResponseTest {

    private static final int SECONDS_IN_DAY = 86400;

    @Test
    void testHttpResponse() {
        // given
        HttpHeader expectedHeaders = new HttpHeader();
        expectedHeaders.put(HOST, "localhost");

        HttpCookie expectedCookies = new HttpCookie();
        Cookie expectedCookie = new Cookie("myCookie", "yummy");
        expectedCookies.putCookie(expectedCookie);

        String[] expectedContentType = new String[]{"text/html"};
        byte[] expectedBody = "this is a body".getBytes();
        int expectedContentLength = expectedBody.length;

        // when
        HttpResponse httpResponse = HttpResponse.builder()
                .status(HttpStatus.OK)
                .header(HOST, "localhost")
                .cookie(expectedCookie)
                .version(HttpVersion.HTTP_1_1)
                .contentType(expectedContentType)
                .body(expectedBody)
                .build();

        // then
        assertThat(httpResponse).extracting(
                        HttpResponse::getStatus,
                        HttpResponse::getHeaders,
                        HttpResponse::getCookies,
                        HttpResponse::getVersion,
                        HttpResponse::getContentType,
                        HttpResponse::getBody,
                        HttpResponse::getContentLength)
                .containsExactly(
                        HttpStatus.OK,
                        expectedHeaders,
                        expectedCookies,
                        HttpVersion.HTTP_1_1,
                        expectedContentType,
                        expectedBody,
                        expectedContentLength);
    }

    @Test
    void testHttpResponseWithMultipleHeaders() {
        // given
        HttpHeader expectedHeaders = new HttpHeader();
        expectedHeaders.put(SET_COOKIE, "cookie1");
        expectedHeaders.put(SET_COOKIE, "cookie2");
        expectedHeaders.put(CONNECTION, KEEP_ALIVE.toString());
        expectedHeaders.put(KEEP_ALIVE, "timeout=5");
        expectedHeaders.put(KEEP_ALIVE, "max=100");

        // when
        HttpResponse httpResponse = HttpResponse.builder()
                .status(HttpStatus.OK)
                .header(SET_COOKIE, "cookie1")
                .header(SET_COOKIE, "cookie2")
                .header(CONNECTION, KEEP_ALIVE.toString())
                .header(KEEP_ALIVE, "timeout=5")
                .header(KEEP_ALIVE, "max=100")
                .build();
        List<String> cookies = httpResponse.getHeaders().getValues(SET_COOKIE);
        List<String> keepAliveValues = httpResponse.getHeaders().getValues(KEEP_ALIVE);

        // then
        assertThat(httpResponse).extracting(
                        HttpResponse::getStatus,
                        HttpResponse::getHeaders)
                .containsExactly(
                        HttpStatus.OK,
                        expectedHeaders);
        assertThat(cookies.size()).isEqualTo(2);
        assertThat(cookies).containsExactly("cookie1", "cookie2");
        assertThat(keepAliveValues).containsExactly("timeout=5", "max=100");
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
