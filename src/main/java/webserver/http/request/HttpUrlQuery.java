package webserver.http.request;

import java.util.Map;

public class HttpUrlQuery {

    private final Map<String, String> queryMap;

    public HttpUrlQuery(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public String getQueryAttribute(String key) {
        return this.queryMap.get(key);
    }
}
