package webserver.http.request;

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

    @Override
    public String toString() {
        return "HttpQueries{" +
                "queryMap=" + queries +
                '}';
    }
}
