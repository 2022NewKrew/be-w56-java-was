package http;

import java.util.Map;

public class RequestParams {

    private final Map<String, String> params;

    public static RequestParams of(Map<String, String> params) {
        return new RequestParams(params);
    }

    private RequestParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String key) {
        return params.getOrDefault(key, "");
    }
}
