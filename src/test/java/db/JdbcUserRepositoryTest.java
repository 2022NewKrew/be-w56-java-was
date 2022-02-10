package db;

import domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class JdbcUserRepositoryTest {

    @Test
    void findByUserIdTest() {
        User user = new User("peach123", "1234", "peach", "peach@kakao.com");
        JdbcUserRepository.addUser(user);

        User result = JdbcUserRepository.findUserById("peach123").orElseThrow(() -> new IllegalArgumentException(""));

        Assertions.assertThat(result).isEqualTo(user);
    }
}
