package http;

import application.CookieKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Cookies {

    private Map<String, Object> attributes;

    public Cookies() {
        this.attributes = new HashMap<>();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return String.valueOf(attributes.getOrDefault(key, ""));
    }

    public String toHeaderString() {
        StringJoiner builder = new StringJoiner(";");
        builder.add(String.format("%s=%s", CookieKeys.LOGINED, attributes.get(CookieKeys.LOGINED)));
        builder.add(String.format("%s=%s", CookieKeys.AUTH_PATH, attributes.get(CookieKeys.AUTH_PATH)));
        return builder.toString();
    }
}
