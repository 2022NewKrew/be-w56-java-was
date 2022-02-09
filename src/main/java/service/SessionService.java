package service;

import db.SessionStorage;
import java.time.LocalDateTime;
import model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final static int SESSION_EXPIRES_TIME = 3;

    public static int setSession(String userId) {
        int sessionId = userId.hashCode();

        SessionStorage.addSession(
            new Session(
                sessionId,
                userId,
                LocalDateTime.now().plusHours(SESSION_EXPIRES_TIME)
            )
        );

        log.debug("session {} is added", sessionId);

        return sessionId;
    }
}
