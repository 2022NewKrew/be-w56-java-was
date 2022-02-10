package app.repository;

import app.configure.DbConfigure;
import app.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);

    private final UserRepository userRepository;

    public UserRepositoryTest() throws SQLException {
        userRepository = new UserRepository(new DbConfigure("localhost"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        userRepository.deleteAll();
    }

    @Test
    void addAndFind() throws SQLException {
        User user = new User("yunyul", "test", "윤렬", "yunyul3@gmail.com");
        userRepository.addUser(user);
        User newUser = userRepository.findUserById(user.getUserId());
        assertThat(user.getUserId()).isEqualTo(newUser.getUserId());
    }

    @AfterEach
    void tearDown() throws SQLException {
        userRepository.deleteAll();
    }
}
