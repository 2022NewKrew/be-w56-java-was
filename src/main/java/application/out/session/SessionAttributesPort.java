package application.out.session;

import domain.session.SessionAttributes;

import java.util.Optional;

public interface SessionAttributesPort {
    void set(String name, Object value);
    Optional<SessionAttributes> get(Long sessionId);
    Optional<SessionAttributes> get(String attributeName);
}
