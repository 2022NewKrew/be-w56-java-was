package application.out.session;

import domain.session.Session;

import java.util.Optional;

public interface SessionPort {
    void set(Session session);
    Optional<Session> get(Long sessionId);
}
