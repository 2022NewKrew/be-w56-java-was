package http.request;

import java.util.Collections;
import java.util.Map;

public class RequestBody {

    private final Map<String, String> params;

    public RequestBody(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }
}
