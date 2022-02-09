package service;

import static org.assertj.core.api.Assertions.assertThat;

import db.SessionStorage;
import org.junit.jupiter.api.Test;

class SessionServiceTest {

    @Test
    void setSession() {
        String userId = "champ";
        int sessionId = SessionService.setSession(userId);

        assertThat(SessionStorage.findSessionById(sessionId)).isNotNull();
    }
}
