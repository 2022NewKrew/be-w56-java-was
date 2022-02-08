package service;

import static org.assertj.core.api.Assertions.assertThat;

import db.DataBase;
import model.SignUpRequest;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class SignUpServiceTest {

    @AfterAll
    static void close() {
        DataBase.clear();
    }

    @Test
    void signUp() {
        // given
        String userId = "champ";
        String password = "test";
        String name = "kakao";
        String email = "champ@kakao.com";
        SignUpRequest signUpRequest = new SignUpRequest(userId, password, name, email);

        // when
        SignUpService.signUp(signUpRequest);

        // then
        User user = DataBase.findUserById(userId);
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }
}
