package http;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;

public class HttpSession {

    public static final String JSESSIONID = "JSESSIONID";

    private final String id;
    private final Map<String, Object> attributes = Maps.newHashMap();

    public HttpSession() {
        this.id = String.valueOf(UUID.randomUUID());
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public String getId() {
        return id;
    }
}
