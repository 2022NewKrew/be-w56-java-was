package http.response;

import http.cookie.Cookie;
import http.HttpMessage;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
import http.view.ViewResolver;
import lombok.Builder;
import lombok.Getter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class HttpResponse extends HttpMessage {
    private final String status;
    private final byte[] body;

    @Builder
    public HttpResponse(HttpProtocolVersion protocolVersion, HttpHeaders headers, List<Cookie> cookies,
                        String status, String uri) {
        super(protocolVersion, headers, cookies);
        this.status = status;
        this.body = uri != null ? ViewResolver.getView(uri) : new byte[0];
    }

    public void send(DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("%s %s\r\n", protocolVersion.getValue(), status));
        for (Map.Entry<String, String> header: headers) {
            dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
        }
        for (Cookie cookie: cookies) {
            dos.writeBytes(String.format("Set-Cookie: %s\r\n", cookie));
        }

        if (body.length > 0) {
            dos.writeBytes(String.format("Content-Length: %d\r\n", body.length));
        }

        dos.writeBytes("\r\n");

        if (body.length > 0) {
            dos.write(body, 0, body.length);
        }

        dos.flush();
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
}
