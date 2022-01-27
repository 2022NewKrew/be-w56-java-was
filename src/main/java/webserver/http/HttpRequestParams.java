package webserver.http;

import java.util.HashMap;
import java.util.Map;
import webserver.util.HttpRequestUtils;

public class HttpRequestParams {

    private final Map<String, String> parameters;

    public HttpRequestParams() {
        this.parameters = new HashMap<>();
    }

    public HttpRequestParams(String queryString) {
        this.parameters = HttpRequestUtils.parseQueryString(queryString);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
