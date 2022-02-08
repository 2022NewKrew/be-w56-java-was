package application.out.session;

import domain.user.SessionAttributes;

import java.util.Optional;

public interface SessionAttributesDao {
    void save(String name, Object value);
    Optional<SessionAttributes> findBySessionId(Long sessionId);
    Optional<SessionAttributes> findByAttributeName(String name);
}
