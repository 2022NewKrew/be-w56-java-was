package webserver.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class HttpRequest {
    private final MethodType method;
    private final String url;
    private final HttpVersion httpVersion;
    private final Map<String, String> queryParams;
}
