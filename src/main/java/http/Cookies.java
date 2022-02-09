package http;

import application.constants.CookieKeys;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.List;
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

    public static Cookies from(List<String> values) {
        Cookies cookies = new Cookies();
        for(String value : values) {
            String[] keyValue = value.split("=");
            cookies.setAttribute(keyValue[0], keyValue[1]);
        }
        return cookies;
    }
}
