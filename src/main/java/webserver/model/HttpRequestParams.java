package webserver.model;

import util.ObjectMappingUtils;

import java.util.Map;

public class HttpRequestParams {
    private final Map<String, String> params;

    public HttpRequestParams(Map<String, String> params) {
        this.params = params;
    }

    public <T> T mapModelObject(Class<T> type) {
        return ObjectMappingUtils.mapObject(params, type);
    }

    @Override
    public String toString() {
        return "HttpRequestParams{" +
                "params=" + params +
                '}';
    }
}
