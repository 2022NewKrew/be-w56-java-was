package http.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeaders {

    private Map<String, String> headers = new HashMap<>();

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
