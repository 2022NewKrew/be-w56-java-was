package model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ValueMap {
    private final Map<String, String> map;

    public ValueMap(final Map<String, String> map) {
        this.map = Objects.requireNonNull(map);
    }

    public Map<String, String> getMap() {
        return Collections.unmodifiableMap(map);
    }
}
