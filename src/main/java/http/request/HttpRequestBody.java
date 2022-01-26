package http.request;

import com.google.common.collect.Maps;

import java.util.Map;

public class HttpRequestBody {
    private final Map<String, String> bodyMap;

    public HttpRequestBody(Map<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public static HttpRequestBody empty() {
        return new HttpRequestBody(Maps.newHashMap());
    }

    @Override
    public String toString() {
        return "HttpRequestBody{" +
                "bodyMap=" + bodyMap +
                '}';
    }

    public String getBody(String name) {
        return bodyMap.get(name);
    }
}
