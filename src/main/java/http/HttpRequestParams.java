package http;

import java.util.Map;

public class HttpRequestParams {
    private final Map<String, String> requestParams;

    public HttpRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestParamName(String name) {
        return requestParams.get(name);
    }

    public void putAll(Map<String, String> params) {
        this.requestParams.putAll(params);
    }
}
