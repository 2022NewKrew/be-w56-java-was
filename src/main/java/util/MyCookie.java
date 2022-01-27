package util;

import java.util.*;

public class MyCookie {

    Map<String, Object> info = new HashMap<>();

    public String get(String key) {
        if (info.containsKey(key)) {
            return (String) info.get(key);
        }
        return null;
    }

    public void set(final String key, final Object value) {
        info.put(key, value);
    }

    public int size() {
        return info.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        info.forEach((key, value) -> stringBuilder.append(String.format("%s=%s; Path=/", key, value)));

        return stringBuilder.toString();
    }

    public boolean isEmpty() {
        return info.isEmpty();
    }
}
