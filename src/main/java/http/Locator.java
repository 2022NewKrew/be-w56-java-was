package http;

import util.HttpRequestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Locator {

    private final String path;
    private final Map<String, String> query;
    private final String fragment;

    public Locator(String path, Map<String, String> query, String fragment) {
        this.path = path;
        this.query = Collections.unmodifiableMap(query);
        this.fragment = fragment;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public String getFragment() {
        return fragment;
    }

    public static Locator parse(String locator) {
        Pattern pattern = Pattern.compile("([^?#]+)(\\?[^#]*)?(#.*)?");
        Matcher matcher = pattern.matcher(locator);
        matcher.find();
        String path = matcher.group(1);
        String queryString = Optional.ofNullable(matcher.group(2))
                .map(s -> s.substring(1))
                .orElse("");
        String fragment = Optional.ofNullable(matcher.group(3))
                .map(s -> s.substring(1))
                .orElse("");
        Map<String, String> query = HttpRequestUtils.parseQueryString(queryString);
        return new Locator(path, query, fragment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locator locator = (Locator) o;
        return Objects.equals(path, locator.path) && Objects.equals(query, locator.query) && Objects.equals(fragment, locator.fragment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, query, fragment);
    }
}
