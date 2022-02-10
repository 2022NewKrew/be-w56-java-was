package service;

import model.user_account.UserAccount;
import model.user_account.UserAccountDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.user.UserAccountNoDbUseRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserAccountNoDbUseRepository repository = new UserAccountNoDbUseRepository();
    UserService userService = new UserService(repository);

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @DisplayName("회원가입 테스트")
    @Test
    void join() {
        //given
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        //when
        String userId = userService.join(userAccountDTO);

        //then
        UserAccount findUserAccount = userService.findOne(userId).get();
        assertThat(userAccountDTO.getUserId()).isEqualTo(findUserAccount.getUserId());
    }

    @Test
    @DisplayName("중복 회원 예외")
    void dupleicateJoin(){
        //givin
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        UserAccountDTO userAccountDTO2 = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("bb")
                .setName("bb")
                .setEmail("bb@com").build();

        //when
        userService.join(userAccountDTO);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(userAccountDTO2));

        assertThat(e.getMessage()).isEqualTo("중복된 userId로 가입 요청을 하였습니다.");

        //then

    }

    @DisplayName("전체 회원 조회 테스트")
    @Test
    void findAll() {
        //givin
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();
        userService.join(userAccountDTO);

        UserAccountDTO userAccountDTO2 = new UserAccountDTO.Builder()
                .setUserId("bb")
                .setPassword("bb")
                .setName("bb")
                .setEmail("bb@com").build();
        userService.join(userAccountDTO2);

        //when
        List<UserAccount> userAccounts = userService.findAll();

        //then
        assertThat(userAccounts.size()).isEqualTo(2);
    }

    @DisplayName("특정 회원 조회 테스트")
    @Test
    void findOne() {
        //givin
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();
        userService.join(userAccountDTO);

        //when
        UserAccount userAccount = userService.findOne(userAccountDTO.getUserId()).get();

        //then
        assertThat(userAccountDTO.getUserId()).isEqualTo(userAccount.getUserId());
    }

    @DisplayName("패스워드 일치 여부 테스트")
    @Test
    void isPasswordEqual() {
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();

        assertThat(userService.isPasswordEqual(new UserAccount(userAccountDTO, 0), "aa")).isEqualTo(true);
        assertThat(userService.isPasswordEqual(new UserAccount(userAccountDTO, 0), "bb")).isEqualTo(false);
    }
}
