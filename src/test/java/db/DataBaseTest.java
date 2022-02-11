package db;

import com.google.common.collect.Maps;
import entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseTest {
    private static final User TEST0 = User.builder()
            .userId("chenID")
            .password("1234")
            .name("chen")
            .email("chen@kakao.com")
            .build();

    private static final User TEST1 = User.builder()
            .userId("chenID1")
            .password("1234")
            .name("chen1")
            .email("chen1@kakao.com")
            .build();

    private static Map<String, User> testUsers = Maps.newHashMap();

    static Stream<Arguments> parseCreate() {
        return Stream.of(
                Arguments.of(TEST0),
                Arguments.of(TEST1)
        );
    }

    @ParameterizedTest(name = "create: {arguments}")
    @MethodSource("parseCreate")
    public void create(User user) {
        testUsers.put(user.getUserId(), user);
        assertThat(testUsers.get(user.getUserId())).isEqualTo(user);
    }
}