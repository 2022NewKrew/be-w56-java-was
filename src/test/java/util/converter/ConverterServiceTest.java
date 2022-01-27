package util.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.domain.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ConverterServiceTest {
    @ParameterizedTest
    @DisplayName("BodyParams에서 User로 Convert 성공 여부 확인")
    @MethodSource("getBodyParamsStream")
    void testConvertBodyParamsToUser(Map<String, String> bodyParams) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //given

        //when
        User user = ConverterService.convert(bodyParams, User.class);

        //then
        assertThat(user.getUserId()).isEqualTo(bodyParams.get("userId"));
        assertThat(user.getPassword()).isEqualTo(bodyParams.get("password"));
        assertThat(user.getName()).isEqualTo(bodyParams.get("name"));
        assertThat(user.getEmail()).isEqualTo(bodyParams.get("email"));
    }


    private static Stream<Arguments> getBodyParamsStream(){
        return getBodyParamsList().stream().map(Arguments::of);
    }

    private static List<Map<String,String>> getBodyParamsList(){
        return List.of(
                Map.of("userId", "javajigi", "password", "password",
                        "name", "javajigi", "email", "java@daum.net"),
                Map.of("userId", "hello", "password", "password2",
                        "name", "hello", "email", "hello@daum.net"),
                Map.of("userId", "hihi", "password", "hihi1234",
                        "name", "hihi", "email", "hihi@daum.net"),
                Map.of("userId", "goodman", "password", "good1234",
                        "name", "good", "email", "good@daum.net")
        );
    }
}