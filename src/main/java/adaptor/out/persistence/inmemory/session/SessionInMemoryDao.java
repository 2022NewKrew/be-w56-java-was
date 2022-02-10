package adaptor.out.persistence.inmemory.session;

import application.out.session.SessionDao;
import domain.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionInMemoryDao implements SessionDao {

    private final static Map<Long, Session> sessions = new HashMap<>();

    @Override
    public void save(Session session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public Optional<Session> findBySessionId(Long sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }
}
