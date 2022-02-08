package repository.session;

import model.UserAccount;
import model.UserAccountDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.Session;
import repository.user.UserAccountNoDbUseRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SessionNoDbUseRepositoryTest {
    SessionNoDbUseRepository repository = new SessionNoDbUseRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @DisplayName("세션 저장 테스트")
    @Test
    void save() {
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        Session session = new Session(new UserAccount(userAccountDTO, 0));

        Session savedSession = repository.save(session);
        Session result = repository.findById(session.getSessionId()).get();
        assertThat(savedSession).isEqualTo(result);
    }

    @DisplayName("값 하나 조회 테스트")
    @Test
    void findById() {
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        Session session = new Session(new UserAccount(userAccountDTO, 0));

        Session savedSession = repository.save(session);

        Session result = repository.findById(session.getSessionId()).get();
        assertThat(result).isEqualTo(savedSession);
    }
}
