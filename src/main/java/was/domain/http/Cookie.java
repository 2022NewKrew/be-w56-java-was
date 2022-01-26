package was.domain.http;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private final Map<String, String> cookies = new HashMap<>();

    public void put(String key, String value) {
        cookies.put(key, value);
    }

    public String get(String key) {
        return cookies.get(key);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        cookies.forEach((key, value) -> sb.append(key).append("=").append(value).append("; "));
        return sb.toString();
    }
}
