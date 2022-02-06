package http.request;

import java.util.Map;

public class Queries {
    private final Map<String, String> queries;

    Queries(Map<String, String> queries) {
        this.queries = queries;
    }

    public String getQuery(String name) {
        return queries.get(name);
    }

    @Override
    public String toString() {
        return "Queries{" +
                "queries=" + queries +
                '}';
    }
}
