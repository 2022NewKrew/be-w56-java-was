package db.repository;

import data.MockUserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.domain.entity.User;
import webserver.domain.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

class JdbcUserRepositoryTest {
    private final UserRepository userRepository = new JdbcUserRepository();

    @BeforeEach
    void setUp(){
        userRepository.saveAll(MockUserData.getAll());
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @DisplayName("데이터베이스에서 사용자 가져오기")
    @MethodSource("data.MockUserData#getUsersStream")
    void get(User user) {
        //given

        //when
        User actualUser = userRepository.getBy(user.getUserId()).orElseThrow();

        //then
        assertThat(actualUser).isEqualTo(user);
    }

    @ParameterizedTest
    @DisplayName("데이터베이스에 사용자 저장")
    @MethodSource("data.MockUserData#getUsersStream")
    void save(User user) {
        //given
        User userForSave = new User(user.getUserId().concat("!"), user.getPassword()
            , user.getName(), user.getEmail());

        //when
        userRepository.save(userForSave);
        User savedUser = userRepository.getBy(userForSave.getUserId()).orElseThrow();

        //then
        assertThat(userForSave).isEqualTo(savedUser);
    }
}