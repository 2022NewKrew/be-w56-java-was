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
        if(!params.containsKey(key)) {
            throw new NullPointerException("해당 파라미터가 존재하지 않습니다.");
        }
        return params.get(key);
    }
}
