package http;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookie {
    private final Path path;
    private final Map<String, String> cookies;

    public Cookie() {
        path = new Path("/");
        cookies = new HashMap<>();
    }

    public void setCookie(String key, Object value) {
        cookies.put(key, value.toString());
    }

    public String createCookieHeader() {
        return cookies.entrySet()
                .stream()
                .map(cookie -> cookie.getKey() + "=" + cookie.getValue())
                .collect(Collectors.joining("; ")) + "; " + path.createHeader();
    }
}
