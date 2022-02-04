package framework.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session 정보를 담을 일급 컬렉션 클래스
 */
public class HttpSession {
    private final Map<String, Object> session = new ConcurrentHashMap<>();

    public Object getAttribute(String key) {
        return session.get(key);
    }

    public void setAttribute(String key, Object value) {
        session.put(key, value);
    }

    public boolean contains(String key) {
        return session.containsKey(key);
    }

    public void invalidate() {
        session.clear();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return session.entrySet();
    }
}
