package http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import webserver.HttpMethod;

public class RequestStartLine {

    private final HttpMethod method;
    private final String url;
    private final String protocol;
    private final Map<String, String> query;

    public RequestStartLine(HttpMethod method, String url, String protocol,
            Map<String, String> query) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.query = Collections.unmodifiableMap(query);
    }

    public static RequestStartLine from(String startLine) {
        List<String> components = List.of(startLine.split(" "));
        HttpMethod method = HttpMethod.valueOf(components.get(0));
        String protocol = components.get(2);
        String url = getUrl(components.get(1));
        String queryString = getQueryString(components.get(1));
        return new RequestStartLine(method, url, protocol, getQueries(queryString));
    }

    private static String getUrl(String urlWithQuery) {
        if (urlWithQuery.contains("?")) {
            return urlWithQuery.split("\\?")[0];
        }
        return urlWithQuery;
    }

    private static String getQueryString(String urlWithQuery) {
        if (urlWithQuery.contains("?")) {
            return urlWithQuery.split("\\?")[1];
        }
        return "";
    }

    private static Map<String, String> getQueries(String queriesString) {
        if (queriesString.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        List<String> queries = List.of(queriesString.split("&"));
        List<String> splitQuery;
        for (String query : queries) {
            splitQuery = List.of(query.split("="));
            result.put(splitQuery.get(0), splitQuery.get(1));
        }

        return Collections.unmodifiableMap(result);
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
