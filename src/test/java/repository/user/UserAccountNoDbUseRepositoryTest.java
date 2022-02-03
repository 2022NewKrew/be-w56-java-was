package repository.user;

import model.UserAccount;
import model.UserAccountDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserAccountNoDbUseRepositoryTest {
    UserAccountNoDbUseRepository repository = new UserAccountNoDbUseRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @DisplayName("디비 저장 테스트")
    @Test
    void save() {
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        UserAccount savedUserAccount = repository.save(userAccountDTO);
        UserAccount result = repository.findById(userAccountDTO.getUserId()).get();
        assertThat(savedUserAccount).isEqualTo(result);
    }

    @DisplayName("값 하나 조회 테스트")
    @Test
    void findById() {
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        UserAccount savedUserAccount = repository.save(userAccountDTO);

        UserAccount result = repository.findById("aa").get();

        assertThat(result).isEqualTo(savedUserAccount);
    }

    @DisplayName("전체 값 조회")
    @Test
    void findAll() {
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();
        repository.save(userAccountDTO);

        UserAccountDTO userAccountDTO2 = new UserAccountDTO.Builder()
                .setUserId("bb")
                .setPassword("bb")
                .setName("bb")
                .setEmail("bb@com").build();
        repository.save(userAccountDTO2);

        List<UserAccount> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
