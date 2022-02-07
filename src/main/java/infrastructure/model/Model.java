package infrastructure.model;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private static final String DELEMETER = "##";
    private final Map<String, String> attributes;

    public Model(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String replaceAll(String origin) {
        for (Map.Entry entry: attributes.entrySet()) {
            origin = replaceAttribute(origin, (String) entry.getKey());
        }
        return origin;
    }

    private String replaceAttribute(String origin, String key) {
        return origin.replaceAll(new StringBuilder(DELEMETER).append(key).append(DELEMETER).toString(), attributes.get(key));
    }

    public static class Builder {
        private Map<String, String> attributes = new HashMap<>();

        public Builder addAttribute(String key, String value) {
            attributes.put(key, value);
            return this;
        }

        public Model build() {
            return new Model(attributes);
        }
    }
}
