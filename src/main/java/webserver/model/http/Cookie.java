package webserver.model.http;

import java.util.ArrayList;
import java.util.List;

public class Cookie {
    private final List<Tuple> pairs = new ArrayList<>();

    public Cookie add(String key, String value) {
        pairs.add(new Tuple(key, value));
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
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
