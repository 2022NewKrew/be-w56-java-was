package repository;

import model.User;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {

    UserRepository repository = new UserRepository();

    @Test
    public void userSaveTest() {
        // given
        User user = new User("testid", "pw", "testname", "testemail");

        // when
        repository.save(user);
    }
}
