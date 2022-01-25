package webserver.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class HttpRequestStartLine {
    private final HttpMethod method;
    private final String targetUri;
    private final String version;
}
