package model.request;

import model.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Headers {
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_SET_COOKIE = "Set-Cookie";
    public static final String HEADER_COOKIE = "Cookie";

    public static final String HEADER_VALUE_SEPARATOR = ": ";
    public static final String HEADER_NEWLINE = "\r\n";

    private final List<Pair> list;

    public Headers(final List<Pair> list) {
        this.list = Objects.requireNonNull(list);
    }

    public List<Pair> getList() {
        return Collections.unmodifiableList(list);
    }

    public Pair getPair(final String key) {
        return list.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElse(Pair.NONE);
    }

    public void updateLocation(final HttpLocation newLocation) {
        final Pair old = getPair(HEADER_LOCATION);
        if (!old.isNone()) {
            list.remove(old);
        }
        list.add(new Pair(HEADER_LOCATION, newLocation.getLocation()));
    }
}
