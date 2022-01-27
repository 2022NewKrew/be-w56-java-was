package http.request;

import exception.InvalidParameterKeyException;
import java.util.Map;

public class RequestBody {

    private final Map<String, String> params;

    public RequestBody(Map<String, String> params) {
        this.params = params;
    }

    public String getValue(String key) {
        if (!params.containsKey(key)) {
            throw new InvalidParameterKeyException();
        }
        return params.get(key);
    }
}
