package http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HttpRequestBody {
    private final String content;

    HttpRequestBody(String content) {
        this.content = URLDecoder.decode(content, StandardCharsets.UTF_8);
    }

    public String content() { return content; }
}
