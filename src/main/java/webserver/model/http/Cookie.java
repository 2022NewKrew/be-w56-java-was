package webserver.model.http;

import java.util.ArrayList;
import java.util.List;

public class Cookie {
    private final List<Tuple> pairs = new ArrayList<>();

    public Cookie() {}

    public Cookie(String value) {
        String[] lines = value.split("; ");
        for (String line : lines) {
            String[] keyAndValue = line.split("=");
            pairs.add(new Tuple(keyAndValue[0], keyAndValue[1]));
        }
    }

    public Cookie add(String key, String value) {
        pairs.add(new Tuple(key, value));
        return this;
    }

    public String get(String key) {
        for (Tuple tuple : pairs)
            if (tuple.key.equals(key))
                return tuple.value;
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Set-Cookie: ");
        for (Tuple tuple : pairs){
            stringBuilder.append(tuple.toString());
            stringBuilder.append("; ");
        }
        return stringBuilder.toString();
    }

    private static class Tuple {
        private final String key;
        private final String value;

        public Tuple(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + "=" + value;
        }
    }
}
