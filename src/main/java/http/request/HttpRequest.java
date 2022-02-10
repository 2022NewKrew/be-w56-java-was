package http.request;

import http.cookie.Cookie;
import http.HttpMessage;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
import http.util.HttpRequestUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class HttpRequest extends HttpMessage {
    private final HttpRequestMethod method;
    private final String uri;
    private final String body;

    @Builder
    public HttpRequest(HttpProtocolVersion protocolVersion, HttpHeaders headers, List<Cookie> cookies,
                       HttpRequestMethod method, String uri, String body) {
        super(protocolVersion, headers, cookies);
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public Map<String, String> parseUrlEncodedBody() {
        return HttpRequestUtils.parseQueryString(body);
    }
}
