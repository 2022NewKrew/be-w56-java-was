package adaptor.out.persistence.session;

import application.out.session.SessionAttributesDao;
import application.out.session.SessionAttributesPort;
import domain.user.SessionAttributes;

import java.util.Optional;

public class SessionAttributesInMemoryPort implements SessionAttributesPort {

    private final SessionAttributesDao sessionAttributesDao;

    public SessionAttributesInMemoryPort(SessionAttributesDao sessionAttributesDao) {
        this.sessionAttributesDao = sessionAttributesDao;
    }

    @Override
    public void set(String name, Object value) {
        sessionAttributesDao.save(name, value);
    }

    @Override
    public Optional<SessionAttributes> get(Long sessionId) {
        return sessionAttributesDao.findBySessionId(sessionId);
    }

    @Override
    public Optional<SessionAttributes> get(String attributeName) {
        return sessionAttributesDao.findByAttributeName(attributeName);
    }
}
