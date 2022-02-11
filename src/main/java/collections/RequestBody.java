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

    public boolean isValidForPost() {
        if (!bodies.containsKey("writer") | !bodies.containsKey("contents")) {
            return false;
        }

        String writer = bodies.get("writer");
        String contents = bodies.get("contents");
        if (writer == null | writer.isBlank() | contents == null | contents.isBlank()) {
            return false;
        }

        return true;
    }
}
