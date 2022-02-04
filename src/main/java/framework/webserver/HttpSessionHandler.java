package framework.webserver;

import framework.util.HttpSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessionHandler {
    private static final Map<String, HttpSession> SESSIONS = new ConcurrentHashMap<>();

    public static HttpSession getSession(String sessionId) {
        return SESSIONS.get(sessionId);
    }

    public static boolean contains(String sessionId) {
        return SESSIONS.containsKey(sessionId);
    }

    public static String makeSession() {
        String sessionId = UUID.randomUUID().toString();
        SESSIONS.put(sessionId, new HttpSession());
        return sessionId;
    }

    public static void makeSessionWithId(String sessionId) {
        SESSIONS.put(sessionId, new HttpSession());
    }
}
