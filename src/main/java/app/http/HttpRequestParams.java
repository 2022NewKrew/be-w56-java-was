package app.http;

import java.util.Map;

public class HttpRequestParams {
    private final Map<String, String> params;

    public static HttpRequestParams of(Map<String, String> params) {
        return new HttpRequestParams(params);
    }

    private HttpRequestParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String key) {
        return params.getOrDefault(key, "");
    }
}
