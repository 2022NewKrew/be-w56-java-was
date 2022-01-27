package was.http;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    public static final int MAX_AGE = 60 * 60;

    public static final String ATTRIBUTE_LOGINED = "Logined";

    public static final String ATTRIBUTE_PATH = "Path";

    public static final String ATTRIBUTE_MAX_AGE = "Max-Age";

    private final Map<String, String> attributes;

    public Cookie() {
        attributes = new HashMap<>();
    }

    public Cookie(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    @Override
    public String toString() {
        return attributes.toString();
    }
}
