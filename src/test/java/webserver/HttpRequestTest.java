package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class HttpRequestTest {

    @Test
    @DisplayName("파라미터를 가지지 않는 GET 요청 메세지 테스트")
    void constructorTest() {
        HttpMethod method = HttpMethod.GET;
        String uri = "/user/create";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "Idea-ff47e0ef=5b4e6969-c1e4-4e3f-9cdf-74399409ea47; JSESSIONID=E47C2A8899B986DCEACC116F2C98DA20");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Connection", "keep-alive");

        HttpRequest httpRequest = new HttpRequest(method, uri, version, headers, "");

        assertThat(httpRequest.getMethod()).isEqualTo(method);
        assertThat(httpRequest.getUri()).isEqualTo(uri);
        assertThat(httpRequest.getVersion()).isEqualTo(version);
        assertThat(httpRequest.getHeaders().get("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getPath()).isEqualTo(uri);
        assertThat(httpRequest.getQueryString().size()).isEqualTo(0);
        assertThat(httpRequest.getCookies().get("Idea-ff47e0ef")).isEqualTo("5b4e6969-c1e4-4e3f-9cdf-74399409ea47");
    }

    @Test
    @DisplayName("파라미터를 가지는 GET 요청 메세지 테스트")
    void constructorTest2() {
        HttpMethod method = HttpMethod.GET;
        String uri = "/user/create?userId=jjj&password=123&name=123&email=dhso%40nnnn.nnn";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "Idea-ff47e0ef=5b4e6969-c1e4-4e3f-9cdf-74399409ea47; JSESSIONID=E47C2A8899B986DCEACC116F2C98DA20");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Connection", "keep-alive");

        HttpRequest httpRequest = new HttpRequest(method, uri, version, headers, "");

        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getQueryString().get("userId")).isEqualTo("jjj");
    }

    @Test
    @DisplayName("데이터를 가지는 POST 요청 메세지 테스트")
    void constructorTest3() {
        HttpMethod method = HttpMethod.POST;
        String uri = "/user/create";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Length", "84");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String body = "userId=jjj&password=123&name=123&email=dhso%40nnnn.nnn";

        HttpRequest httpRequest = new HttpRequest(method, uri, version, headers, body);

        assertThat(httpRequest.getBodyMap().get("userId")).isEqualTo("jjj");
        assertThat(httpRequest.getBodyMap().get("password")).isEqualTo("123");
        assertThat(httpRequest.getBodyMap().get("name")).isEqualTo("123");
        assertThat(httpRequest.getBodyMap().get("email")).isEqualTo("dhso%40nnnn.nnn");
    }



}
