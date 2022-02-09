package util.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.domain.entity.User;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ConverterServiceTest {
    @ParameterizedTest
    @DisplayName("BodyParams에서 User로 Convert 성공 여부 확인")
    @MethodSource("data.MockUserData#getFieldMapStream")
    void testConvertBodyParamsToUser(Map<String, String> bodyParams) {
        //given

        //when
        User user = ConverterService.convert(bodyParams, User.class);

        //then
        assertThat(user.getUserId()).isEqualTo(bodyParams.get("userId"));
        assertThat(user.getPassword()).isEqualTo(bodyParams.get("password"));
        assertThat(user.getName()).isEqualTo(bodyParams.get("name"));
        assertThat(user.getEmail()).isEqualTo(bodyParams.get("email"));
    }
}