package http.request;

import http.util.HttpRequestUtils;

import java.util.Map;

public class RequestParams {

    private Map<String, String> params;

    private RequestParams(Map<String, String> params) {
        this.params = params;
    }

    public static RequestParams createRequestParams(String queryString) {
        return new RequestParams(HttpRequestUtils.parseQueryString(queryString));
    }

    public String getValue(String key) {
        return params.getOrDefault(key, "");
    }
}
