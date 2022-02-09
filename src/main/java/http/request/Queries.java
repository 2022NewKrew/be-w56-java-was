package http.request;

import java.util.HashMap;
import java.util.Map;

public class Queries {

    private final Map<String, String> params;

    public Queries(Map<String, String> params) {
        this.params = params;
    }

    public static Queries empty() {
        return new Queries(new HashMap<>());
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getValue(String key) {
        return params.get(key);
    }
}
