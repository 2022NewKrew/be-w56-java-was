package util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpRequest {
    private final RequestMethod requestMethod;
    private final String uri;
    private final String httpVersion;

    @Builder
    public HttpRequest(RequestMethod requestMethod, String uri, String httpVersion) {
        this.requestMethod = requestMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }
}
