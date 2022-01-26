package http;

import java.util.Map;

public class RequestBody {

    private final Map<String, String> body;

    public static RequestBody of(Map<String, String> body) {
        return new RequestBody(body);
    }

    private RequestBody(Map<String, String> body) {
        this.body = body;
    }

    public String get(String key) {
        return body.getOrDefault(key, "");
    }
}
