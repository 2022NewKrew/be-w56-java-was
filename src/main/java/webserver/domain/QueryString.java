package webserver.domain;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class QueryString {

    private final Map<String, String> queryStrings;

    private QueryString(Map<String, String> queryStrings) {
        this.queryStrings = new HashMap<>(queryStrings);
    }

    public QueryString(QueryString queryString) {
        this(queryString.queryStrings);
    }

    public static QueryString createQueryString(String queryString) {
        return new QueryString(HttpRequestUtils.parseQueryString(queryString));
    }

    public String getQueryStringAttribute(String key) {
        return queryStrings.get(key);
    }
}
