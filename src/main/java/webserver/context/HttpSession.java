package webserver.context;

import myspring.users.UserDto;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    private final String cookie;

    private final Map<String, Object> data;

    public HttpSession(String cookie) {
        this.cookie = cookie;
        this.data = new HashMap<>();
    }

    public String getCookie() {
        return cookie;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object getAttribute(String key){
        if (data.containsKey(key)) return data.get(key);
        return null;
    }

    public void setAttribute(String key, Object value) {
        data.put(key, value);
    }

    public void invalidate() {
        data.clear();
    }
}
