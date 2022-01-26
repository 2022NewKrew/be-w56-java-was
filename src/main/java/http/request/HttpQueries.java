package http.request;

import java.util.HashMap;
import java.util.Map;

public class HttpQueries {
    private final Map<String, String> queries;

    public HttpQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public HttpQueries() {
        this(new HashMap<>());
    }

    public String getQuery(String name) {
        return queries.get(name);
    }

    @Override
    public String toString() {
        return "HttpQueries{" +
                "queryMap=" + queries +
                '}';
    }
}
