package webserver.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Cookies {
    private final Map<String, Cookie> cookies;

    public Cookies() {
        this.cookies = new HashMap<>();
    }

    public void addCookie(String name, String value) {
        cookies.put(name, new Cookie(name, value));
    }

    public Optional<Cookie> getCookieByName(String name) {
        return Optional.ofNullable(cookies.get(name));
    }

    public String getHttpHeader() {
        return cookies.values()
                .stream()
                .map(Cookie::getHttpHeader)
                .collect(Collectors.joining("; "));
    }

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

}
