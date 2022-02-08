package app.http;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class HttpRequest {
    private HttpMethod method;
    private String url;
    private HttpVersion version;
    private HttpHeader header;
    private HttpRequestParams params;
    private HttpRequestBody body;

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public static HttpRequest of() {
        return new HttpRequest();
    }

    private HttpRequest() {
        this.header = HttpHeader.of();
    }

    public void put(String key, String value) {
        this.header.put(key, value);
    }

    public String get(String key, String defaultValue) {
        return this.header.get(key, defaultValue);
    }
}
