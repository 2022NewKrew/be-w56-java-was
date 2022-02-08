package http;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private static final String EQUAL = "=";
    private static final String SEPARATOR = ";";

    private final Map<String, String> keyValues = new HashMap<>();

    public Cookie() {}

    public void set(String key, String value) {
        keyValues.put(key, value);
    }

    public String get(String key) {
        if (!keyValues.containsKey(key)) {
            return "";
        }
        return keyValues.get(key);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        keyValues.forEach((k,v)->{
            stringBuilder.append(k).append(EQUAL).append(v).append(SEPARATOR);
        });

        if (stringBuilder.length() < 1) {
            return "";
        }

        return stringBuilder.append("Path=/").toString();
    }
}
