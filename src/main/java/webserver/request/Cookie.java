package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    private Map<String, String> cookieMap;

    public Cookie() {
        this.cookieMap = new HashMap<>();
    }

    public Cookie(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public String getCookieByKey(String key) {
        return cookieMap.get(key);
    }
}
