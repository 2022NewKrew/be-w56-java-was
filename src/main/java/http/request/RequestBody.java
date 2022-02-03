package http.request;

import exception.InvalidParameterKeyException;
import java.util.Collections;
import java.util.Map;

public class RequestBody {

    private final Map<String, String> params;

    public RequestBody(Map<String, String> params) {
        this.params = params;
    }

    public static RequestBody empty() {
        return new RequestBody(Collections.emptyMap());
    }

    public String getValue(String key) {
        if (!params.containsKey(key)) {
            throw new InvalidParameterKeyException();
        }
        return params.get(key);
    }
}
