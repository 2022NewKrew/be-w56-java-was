package http.request;

import java.util.Map;

public class RequestParams {

    private Map<String, Object> params;

    private RequestParams(Map<String, Object> params) {
        this.params = params;
    }

    public static RequestParams of(Map<String, Object> params) {
        return new RequestParams(params);
    }

    public Object getValue(String key) {
        return params.getOrDefault(key, new Object());
    }
}
