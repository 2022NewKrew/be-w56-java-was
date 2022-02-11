package dto;

import entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class UserSignUpDtoTest {
    private static final User TEST0 = User.builder()
            .userId("chenID")
            .password("1234")
            .name("chen")
            .email("chen@kakao.com")
            .build();

    @ParameterizedTest(name = "create: {arguments}")
    @CsvSource(value = {"chenID,1234,chen,chen@kakao.com"}, delimiter = ',')
    void userSignUpDtoToEntity(String userId, String password, String name, String email) {
        assertThat(new UserSignUpDto(userId, password, name, email).toEntity())
                .isEqualTo(TEST0);
    }
}