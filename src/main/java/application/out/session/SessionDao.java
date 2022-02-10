package application.out.session;

import domain.session.Session;

public interface SessionDao {
    void save(Session session);
    Session findBySessionId(String sessionId);
    void delete(String sessionId);
}
