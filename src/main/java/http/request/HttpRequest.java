package http.request;

import http.HttpHeaders;
import http.HttpMessage;
import lombok.Builder;
import lombok.Getter;

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
}
