package application.session;

import application.exception.session.NonExistsSessionException;
import application.exception.session.SessionExpiredException;
import application.in.session.GetSessionUseCase;
import application.out.session.SessionPort;
import domain.session.Session;

public class GetSessionService implements GetSessionUseCase {

    private final SessionPort sessionPort;

    public GetSessionService(SessionPort sessionPort) {
        this.sessionPort = sessionPort;
    }

    @Override
    public Session getSession(String sessionId) {
        Session session = sessionPort.get(sessionId)
                .orElseThrow(NonExistsSessionException::new);

        if (session.isExpired()) {
            sessionPort.remove(sessionId);
            throw new SessionExpiredException();
        }

        return session;
    }
}
