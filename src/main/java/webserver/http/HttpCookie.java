package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class HttpCookie {

    private final Map<String, Cookie> cookieMap;

    public HttpCookie() {
        cookieMap = new HashMap<>();
    }

    public boolean containsCookie(String name) {
        return cookieMap.containsKey(name);
    }

    public <X extends Throwable> String orElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name).getValue();
        } else {
            throw exceptionSupplier.get();
        }
    }

    public void putCookie(Cookie cookie) {
        cookieMap.put(cookie.getName(), cookie);
    }

    public Optional<String> getValue(String name) {
        Cookie cookie = cookieMap.get(name);
        if (cookie == null) {
            return Optional.empty();
        }
        return Optional.of(cookie.getValue());
    }

    public Iterable<Cookie> iterator() {
        return cookieMap.values();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        cookieMap.forEach((k, v) -> stringBuilder.append(v));
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        return cookieMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpCookie)) {
            return false;
        }
        HttpCookie c = (HttpCookie) obj;
        return this.cookieMap.equals(c.cookieMap);
    }
}
