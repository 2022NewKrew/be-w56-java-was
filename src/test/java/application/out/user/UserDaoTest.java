package application.out.user;

import adaptor.out.persistence.mysql.QueryBuilder;
import adaptor.out.persistence.mysql.user.UserMysqlDao;
import domain.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    UserDao userDao = new UserMysqlDao(new QueryBuilder("user", List.of("id", "password", "name", "email")));

    @Test
    @Disabled
    void save() {
        User user = User.builder()
                .userId("id123")
                .password("password")
                .name("name")
                .email("email@email.com")
                .build();
        userDao.save(user);
    }

    @Test
    @Disabled
    void findByUserId() {
        User user = userDao.findByUserId("id");
        assertThat(user)
                .isNotNull();
        assertThat(user.getUserId())
                .isEqualTo("id");
    }

    @Test
    void findAll() {
        List<User> all = userDao.findAll();

        assertThat(all.size()).isEqualTo(3);
    }
}