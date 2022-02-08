package webserver.http.request;

import java.util.Map;

public class HttpBody {

    private final Map<String, String> bodyMap;

    public HttpBody(Map<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public String getBodyAttribute(String key) {
        return bodyMap.get(key);
    }

    public int valueCount() {
        return bodyMap.values().size();
    }
}
