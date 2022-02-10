package application.session;

import application.exception.session.NonExistsSessionException;
import application.in.session.SetSessionUseCase;
import application.out.session.SessionAttributesPort;
import application.out.session.SessionPort;
import domain.session.Session;

public class SetSessionService implements SetSessionUseCase {

    private final static int EXPIRY_HOUR = 3;
    private final SessionPort sessionPort;
    private final SessionAttributesPort sessionAttributesPort;

    public SetSessionService(SessionPort sessionPort, SessionAttributesPort sessionAttributesPort) {
        this.sessionPort = sessionPort;
        this.sessionAttributesPort = sessionAttributesPort;
    }

    @Override
    public Long setSession(String name, Object value) {
        sessionAttributesPort.set(name, value);
        Long sessionId = sessionAttributesPort.get(name)
                .orElseThrow(NonExistsSessionException::new)
                .getSessionId();

        Session session = Session.create(sessionId, EXPIRY_HOUR);
        sessionPort.set(session);

        return sessionId;
    }
}
