package webapp.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String uri;
    private final String httpVersion;
    private final Map<String, String> queryStrings;

    @Builder
    public HttpRequest(HttpMethod httpMethod, String uri, String httpVersion,
                       Map<String, String> queryStrings) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.queryStrings = queryStrings;
    }
}
