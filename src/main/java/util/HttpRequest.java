package util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String uri;
    private final String httpVersion;

    @Builder
    public HttpRequest(HttpMethod httpMethod, String uri, String httpVersion) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }
}
