package http.request;

import exception.InvalidParameterKeyException;
import java.util.Map;

public class Queries {

    private final Map<String, String> params;

    public Queries(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getValue(String key) {
        if (!params.containsKey(key)) {
            throw new InvalidParameterKeyException();
        }
        return params.get(key);
    }
}
