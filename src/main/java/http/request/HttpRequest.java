package http.request;

import http.HttpMessage;
import http.header.HttpHeaders;
import http.util.HttpRequestUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest extends HttpMessage {
    private final HttpRequestMethod method;
    private final String uri;
    private final String body;

    @Builder
    public HttpRequest(String protocolVersion, HttpHeaders headers, String body,
                       HttpRequestMethod method, String uri) {
        super(protocolVersion, headers);
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public Map<String, String> parseQueryParams() {
        String[] tokens = uri.split("\\?", 2);
        if (tokens.length < 2) {
            return new HashMap<>();
        }
        return HttpRequestUtils.parseQueryString(tokens[1]);
    }

    public Map<String, String> parseUrlEncodedBody() {
        return HttpRequestUtils.parseQueryString(body);
    }
}
