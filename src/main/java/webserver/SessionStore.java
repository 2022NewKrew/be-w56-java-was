package webserver;

import com.google.common.collect.Maps;
import http.HttpSession;
import java.util.Map;
import java.util.Optional;

public class SessionStore {

    private static final Map<String, HttpSession> sessions = Maps.newConcurrentMap();

    private SessionStore() {
    }

    public static void add(String sessionId, HttpSession httpSession) {
        sessions.put(sessionId, httpSession);
    }

    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    public static Optional<HttpSession> find(String sessionId) {
        if (sessionId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.get(sessionId));
    }
}
