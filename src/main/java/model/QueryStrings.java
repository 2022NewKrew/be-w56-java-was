package model;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class QueryStrings {
    private final Map<String, String> queryStrings;

    private QueryStrings(Map<String, String> queryStrings) {
        this.queryStrings = new HashMap<>(queryStrings);
    }

    public static QueryStrings from(String queryStrings) {
        return new QueryStrings(HttpRequestUtils.parseQueryString(queryStrings));
    }

    public Map<String, String> getQueryStrings() {
        return queryStrings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String key : queryStrings.keySet()) {
            sb.append(key).append(" : ").append(queryStrings.get(key)).append("\n");
        }

        return "QueryStrings{" +
                "\n queryStrings=" + sb +
                '}';
    }
}
