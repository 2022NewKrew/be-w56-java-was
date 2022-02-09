package db;

import com.google.common.collect.Maps;
import java.util.Map;
import model.Session;

public class SessionStorage {

    private static final Map<Integer, Session> sessions = Maps.newHashMap();

    public static void addSession(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    public static Session findSessionById(int sessionId) {
        return sessions.get(sessionId);
    }
}
