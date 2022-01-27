package http.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import webserver.HttpMethod;

public class RequestStartLine {
    private final HttpMethod method;
    private final String url;
    private final String protocol;
    private final Map<String, String> query;

    public RequestStartLine(HttpMethod method, String url, String protocol, Map<String, String> query) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.query = query;
    }

    public RequestStartLine(HttpMethod method, String url, String protocol, String queryString) {
        this(method, url, protocol, new HashMap<>());
        setQuery(queryString);
    }
    public RequestStartLine(HttpMethod method, String url, String protocol) {
        this(method, url, protocol, new HashMap<>());
    }

    private void setQuery(String queriesString) {
        List<String> queries = List.of(queriesString.split("&"));
        List<String> splitQuery;
        for (String query : queries) {
            splitQuery = List.of(query.split("="));
            this.query.put(splitQuery.get(0), splitQuery.get(1));
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
