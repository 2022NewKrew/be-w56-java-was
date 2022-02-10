package application.session;

import application.in.session.SetSessionUseCase;
import application.out.session.SessionPort;
import domain.session.Session;
import domain.session.SessionId;

import java.time.Duration;

public class SetSessionService implements SetSessionUseCase {

    private final static int EXPIRY_HOUR = 3;
    private final SessionPort sessionPort;

    public SetSessionService(SessionPort sessionPort) {
        this.sessionPort = sessionPort;
    }

    @Override
    public String setSession(String name, String value) {
        SessionId sessionId = SessionId.create();
        Session<String> session = new Session(sessionId, name, value, Duration.ofHours(EXPIRY_HOUR));

        sessionPort.set(session);
        return sessionId.getValue();
    }
}
