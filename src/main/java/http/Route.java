package http;

import java.util.Objects;

public class Route {

    private final Method method;
    private final String pattern;

    public Route(Method method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public boolean matches(Request request) {
        return request.getMethod().equals(method) && request.getPath().matches(pattern);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(method, route.method) && Objects.equals(pattern, route.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, pattern);
    }
}
