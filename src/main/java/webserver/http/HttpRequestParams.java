package webserver.http;

import java.util.Map;
import webserver.util.HttpRequestUtils;

public class HttpRequestParams {

    private final Map<String, String> parameters;

    public HttpRequestParams(String queryString) {
        this.parameters = HttpRequestUtils.parseQueryString(queryString);

    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
