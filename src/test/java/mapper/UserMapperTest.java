package mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import dto.UserDto;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserMapper 테스트")
class UserMapperTest {

    @DisplayName("User to UserDto 테스트")
    @Test
    void userToDto() {
        UserMapper userMapper = UserMapper.instance;
        User user = new User("userId", "password", "name", "email");
        UserDto testDto = userMapper.userToDto(user);

        assertThat(testDto.getUserId()).isEqualTo(user.getUserId());
        assertThat(testDto.getName()).isEqualTo(user.getName());
        assertThat(testDto.getEmail()).isEqualTo(user.getEmail());
    }
}
