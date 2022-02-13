import model.User;
import model.repository.user.UserRepository;
import model.repository.user.UserRepositoryMap;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryMapTest {
    @Test
    void save() throws IOException, IllegalAccessException {
        UserRepository userRepository = new UserRepositoryMap();

        User userSaved = userRepository.save(User.builder()
                .stringId("test1")
                .name("testName1")
                .build());
        System.out.println(userSaved.getId());
        assertThat(userSaved.getId()).isEqualTo(1);

        userSaved = userRepository.save(User.builder()
                .stringId("test2")
                .name("testName2")
                .build());
        System.out.println(userSaved.getId());
        assertThat(userSaved.getId()).isEqualTo(2);
    }
}
