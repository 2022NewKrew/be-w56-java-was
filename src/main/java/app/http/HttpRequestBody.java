package app.http;

import java.util.Map;

public class HttpRequestBody {
    private final Map<String, String> body;

    public static HttpRequestBody of(Map<String, String> body) {
        return new HttpRequestBody(body);
    }

    private HttpRequestBody(Map<String, String> body) {
        this.body = body;
    }

    public String get(String key) {
        return body.getOrDefault(key, "");
    }

    public Map<String, String> getBody() {
        return body;
    }
}
