package adaptor.out.persistence.session;

import application.out.session.SessionAttributesDao;
import domain.user.SessionAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class SessionAttributeInMemoryDao implements SessionAttributesDao {

    private final static Map<Long, SessionAttributes> attributes = new HashMap<>();
    private final static AtomicLong id = new AtomicLong();

    @Override
    public void save(String name, Object value) {
        long sessionId = id.getAndIncrement();
        SessionAttributes sessionAttributes = new SessionAttributes(sessionId, name, value);
        attributes.put(sessionId, sessionAttributes);
    }

    @Override
    public Optional<SessionAttributes> findBySessionId(Long sessionId) {
        return Optional.ofNullable(attributes.get(sessionId));
    }

    @Override
    public Optional<SessionAttributes> findByAttributeName(String name) {
        return attributes.entrySet().stream()
                .filter(e -> e.getValue().getAttributeName().equals(name))
                .map(e -> e.getValue())
                .findFirst();
    }
}
