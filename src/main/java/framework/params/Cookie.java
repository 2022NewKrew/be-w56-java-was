package framework.params;

import java.util.Map;

public class Cookie {
    private final Map<String, String> cookie;

    public Cookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }

    public boolean isEmpty() {
        return cookie.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        cookie.entrySet().forEach(entry -> builder.append(entry.toString()));
        return builder.toString();
    }
}
