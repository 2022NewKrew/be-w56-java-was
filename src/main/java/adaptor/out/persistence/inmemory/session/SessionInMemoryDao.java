package adaptor.out.persistence.inmemory.session;

import application.out.session.SessionDao;
import domain.session.Session;

import java.util.HashMap;
import java.util.Map;

public class SessionInMemoryDao implements SessionDao {

    private final static Map<String, Session> sessions = new HashMap<>();

    @Override
    public void save(Session session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public Session findBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public void delete(String sessionId) {
        sessions.remove(sessionId);
    }
}
