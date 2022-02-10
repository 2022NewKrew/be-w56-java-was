package application.out.session;

import domain.session.Session;

import java.util.Optional;

public interface SessionDao {
    void save(Session session);
    Optional<Session> findBySessionId(Long sessionId);
}
