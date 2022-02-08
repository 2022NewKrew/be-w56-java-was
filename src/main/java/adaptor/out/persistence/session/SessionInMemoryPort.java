package adaptor.out.persistence.session;

import application.out.session.SessionDao;
import application.out.session.SessionPort;
import domain.user.Session;

import java.util.Optional;

public class SessionInMemoryPort implements SessionPort {

    private final SessionDao sessionDao;

    public SessionInMemoryPort(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public void set(Session session) {
        sessionDao.save(session);
    }

    @Override
    public Optional<Session> get(Long sessionId) {
        return sessionDao.findBySessionId(sessionId);
    }
}
