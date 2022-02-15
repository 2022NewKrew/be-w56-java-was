package webserver.http.request.header;

import util.HttpRequestUtils;

import java.util.Collections;
import java.util.Map;

public class HeaderMap {
    private final Map<String, String> map;

    public HeaderMap(String inputHeader) {
        this.map = Collections.unmodifiableMap(getHeaderMapByInput(inputHeader));
    }

    private Map<String, String> getHeaderMapByInput(String inputHeader) {
        return HttpRequestUtils.parseQueryString(inputHeader, System.lineSeparator(), ": ");
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public String get(String key) {
        return map.get(key);
    }
}
