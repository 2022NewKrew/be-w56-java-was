package webserver.http.request.body;

import util.HttpRequestUtils;

import java.util.Map;

public class BodyMap {

    private final Map<String, String> map;

    public BodyMap(String inputBody) {
        map = Map.copyOf(getBodyMapByInput(inputBody));
    }

    private Map<String, String> getBodyMapByInput(String inputBody) {
        return HttpRequestUtils.parseQueryString(inputBody, "&", "=");
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public String get(String key) {
        return map.get(key);
    }

    public Map<String, String> getInstance() {
        return Map.copyOf(map);
    }
}
