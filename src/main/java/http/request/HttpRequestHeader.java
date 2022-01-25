package http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> headers = new HashMap<>();

    public void addHeaderLine(String key, String value){
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
