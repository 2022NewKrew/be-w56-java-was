package util;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MyHeaders {

    private static final String HEADER_SEPARATOR = ": ";
    private final Map<String, Pair> info = new LinkedHashMap<>();
    private final MyCookie myCookie = new MyCookie();

    public String get(String key) {
        if (info.containsKey(key.toLowerCase(Locale.ROOT))) {
            return info.get(key.toLowerCase(Locale.ROOT)).getValue();
        }

        return null;
    }

    public void set(final String header) {
        String[] keyValue = header.split(HEADER_SEPARATOR);

        String key = keyValue[0].trim();
        String value = keyValue[1].trim();

        Pair pair = new Pair(key, value);

        if ("Cookie".equals(key)) {
            myCookie.set(value);
        } else {
            info.put(key.toLowerCase(Locale.ROOT), pair);
        }
    }

    public MyCookie getMyCookie() {
        return myCookie;
    }

    private static class Pair {

        private final String key;
        private final String value;

        public Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        return info.toString();
    }
}
