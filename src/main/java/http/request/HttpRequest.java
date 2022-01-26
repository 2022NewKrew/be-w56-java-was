package http.request;

import http.header.HttpHeaders;
import http.HttpMessage;
import http.util.HttpRequestUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class HttpRequest extends HttpMessage {
    private final HttpRequestMethod method;
    private final String uri;
    private final String body;

    @Builder
    public HttpRequest(String protocolVersion, HttpHeaders headers,
                       HttpRequestMethod method, String uri, String body) {
        super(protocolVersion, headers);
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public Map<String, String> getQueryParams() {
        String queryStr = uri.split("\\?", 2)[1];
        return HttpRequestUtils.parseQueryString(queryStr);
    }
}
