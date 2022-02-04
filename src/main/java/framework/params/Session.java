package framework.params;

import com.google.common.collect.Maps;

import java.util.Map;

public class Session {
    Map<String, String> session = Maps.newConcurrentMap();

    public Map<String, String> getSession() {
        return session;
    }

    public void setAttributes(String key, String value) {
        session.put(key, value);
    }
}
