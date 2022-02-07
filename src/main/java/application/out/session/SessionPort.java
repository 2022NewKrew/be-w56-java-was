package application.out.session;

import domain.user.Session;

import java.util.Optional;

public interface SessionPort {
    void set(Session session);
    Optional<Session> get(Long sessionId);
}
