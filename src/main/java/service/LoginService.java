package service;

import db.SessionStorage;
import db.UserStorage;
import java.sql.SQLException;
import java.time.LocalDateTime;
import model.LoginRequest;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {

    public static final int LOGIN_FAILED = -1;
    private final static int SESSION_EXPIRES_TIME = 3;
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public static int login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException {
        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();

        log.debug("User {} tries to login", userId);

        User user = UserStorage.findUserById(userId);

        if (user == null) {
            log.debug("User {} Not Found", userId);
            return LOGIN_FAILED;
        }

        if (!user.passwordMatch(password)) {
            log.debug("User {} password mismatch", userId);
            return LOGIN_FAILED;
        }

        int sessionId = setSession(loginRequest.getUserId());

        log.debug("Login Success");
        return sessionId;
    }

    private static int setSession(String userId) throws SQLException, ClassNotFoundException {
        int sessionId = userId.hashCode();

        Session session = SessionStorage.findSessionById(sessionId);
        if (session != null) {
            return updateSession(userId, sessionId);
        }

        return addSession(userId, sessionId);
    }

    private static int updateSession(String userId, int sessionId) throws SQLException, ClassNotFoundException {
        SessionStorage.updateSession(
            new Session(
                sessionId,
                userId,
                LocalDateTime.now().plusHours(SESSION_EXPIRES_TIME)
            )
        );

        log.debug("session {} is updated", sessionId);

        return sessionId;
    }

    private static int addSession(String userId, int sessionId) throws SQLException, ClassNotFoundException {
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
