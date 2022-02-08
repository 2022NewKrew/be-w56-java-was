package application.session;

import application.exception.session.NonExistsSessionException;
import application.exception.session.SessionExpiredException;
import application.in.session.GetSessionUseCase;
import application.out.session.SessionAttributesPort;
import application.out.session.SessionPort;

import java.time.LocalDateTime;

public class GetSessionService implements GetSessionUseCase {

    private final SessionPort sessionPort;
    private final SessionAttributesPort sessionAttributesPort;

    public GetSessionService(SessionPort sessionPort, SessionAttributesPort sessionAttributesPort) {
        this.sessionPort = sessionPort;
        this.sessionAttributesPort = sessionAttributesPort;
    }

    @Override
    public Object getSession(Long sessionId) {
        if (sessionPort.get(sessionId)
                .orElseThrow(NonExistsSessionException::new)
                .isExpired(LocalDateTime.now())) {
            throw new SessionExpiredException();
        }

        return sessionAttributesPort.get(sessionId).orElseThrow(NonExistsSessionException::new)
                .getAttributeValue();
    }
}
