package http.response;

import http.HttpMessage;
import http.cookie.Cookie;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
import http.view.ViewResolver;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class HttpResponse extends HttpMessage {
    private final String status;
    private final byte[] body;

    @Builder
    public HttpResponse(HttpProtocolVersion protocolVersion, HttpHeaders headers, List<Cookie> cookies,
                        String status, String uri, Map<String, Object> model) {
        super(protocolVersion, headers, cookies);
        this.status = status;
        this.body = (uri != null ? ViewResolver.getView(uri, model) : new byte[0]);
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void removeCookie(String name) {
        cookies.removeIf(x -> x.getName().equals(name));
    }

    // --------------------------------------------------------------------------------
    // Factory methods
    // TODO: 재사용 가능하도록 리팩토링
    public static HttpResponse ok(String path) {
        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersion.HTTP_1_1)
                .headers(new HttpHeaders())
                .cookies(new ArrayList<>())
                .status("200 OK")
                .uri(path)
                .build();
    }

    public static HttpResponse redirect(String redirectLocation) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectLocation);

        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersion.HTTP_1_1)
                .headers(headers)
                .cookies(new ArrayList<>())
                .status("302 Found")
                .build();
    }

    public static HttpResponse badRequest() {
        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersion.HTTP_1_1)
                .headers(new HttpHeaders())
                .cookies(new ArrayList<>())
                .status("400 Bad Request")
                .build();
    }

    public static HttpResponse notFound() {
        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersion.HTTP_1_1)
                .headers(new HttpHeaders())
                .cookies(new ArrayList<>())
                .status("404 Not Found")
                .build();
    }

    public static HttpResponse okTemplate(String path, Pattern pattern, String toReplaceWith) {
        Map<String, Object> model = new HashMap<>();
        model.put("pattern", pattern);
        model.put("toReplaceWith", toReplaceWith);

        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersion.HTTP_1_1)
                .headers(new HttpHeaders())
                .cookies(new ArrayList<>())
                .status("200 OK")
                .uri(path)
                .model(model)
                .build();
    }
}
