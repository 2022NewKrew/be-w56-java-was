package webserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HttpRequestStartLine {
    private String method;
    private String url;
    private String version;
}
