package collections;

import util.HttpRequestUtils;

import java.util.Map;

public class RequestBody {

    private Map<String, String> bodies;

    public RequestBody(Map<String, String> bodies) {
        this.bodies = bodies;
    }

    public RequestBody(String content) {
        this.bodies = HttpRequestUtils.parseValues(content, "&");
    }

    public Map<String, String> getBodies() {
        return bodies;
    }

    public String getParameter(String key) {
        return bodies.get(key);
    }

}
