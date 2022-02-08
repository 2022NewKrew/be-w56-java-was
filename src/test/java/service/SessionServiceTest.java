package service;

import model.UserAccount;
import model.UserAccountDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.session.SessionNoDbUseRepository;
import repository.user.UserAccountNoDbUseRepository;
import webserver.session.Session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {
    SessionNoDbUseRepository repository = new SessionNoDbUseRepository();
    SessionService sessionService = SessionService.getInstance();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @DisplayName("세션 디비 저장 테스트")
    @Test
    void join() {
        //given
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        UserAccount userAccount = new UserAccount(userAccountDTO, 0);
        Session session = new Session(userAccount);

        //when
        String userId = sessionService.join(session);

        //then
        Session findSession = sessionService.findOne(session.getSessionId()).get();
        assertThat(userAccountDTO.getUserId()).isEqualTo(findSession.getSessionId());
    }

    @DisplayName("특정 세션 조회")
    @Test
    void findOne() {
        //givin
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        UserAccount userAccount = new UserAccount(userAccountDTO, 0);
        Session session = new Session(userAccount);
        sessionService.join(session);

        //when
        Session findSession = sessionService.findOne(userAccountDTO.getUserId()).get();

        //then
        assertThat(userAccountDTO.getUserId()).isEqualTo(findSession.getSessionId());
    }
}
