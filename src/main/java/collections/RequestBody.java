package collections;

import util.HttpRequestUtils;

import java.util.Map;

public class RequestBody {

    private Map<String, String> bodies;

    public RequestBody(String content) {
        this.bodies = HttpRequestUtils.parseValues(content, "&");
    }

    public Map<String, String> getBodies() {
        return bodies;
    }

}
