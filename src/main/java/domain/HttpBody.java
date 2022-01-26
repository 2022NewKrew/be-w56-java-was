package domain;

import java.util.Map;

public class HttpBody {

    private final Map<String, String> body;

    public HttpBody(Map<String, String> httpBody) {
        this.body = httpBody;
    }

    public String get(String key) {
        return body.get(key);
    }
}
