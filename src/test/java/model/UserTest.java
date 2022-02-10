package model;

import exceptions.InvalidRequestFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UserTest {

    private final String userId = "userId";
    private final String password = "password";
    private final String name = "name";
    private final String email = "email";

    @Test
    @DisplayName("[성공] User 객체를 생성한다")
    void User() {
        new User(userId, password, name, email);
    }

    @DisplayName("[성공] User 객체 생성 - Null 입력")
    @ParameterizedTest(name = "{0}, {1}, {2}, {3}")
    @CsvSource(value = {"null, password, name, email@test", "userId, null, name, email@test",
            "userId, password, null, email@test", "userId, password, name, null"}, nullValues = {"null"})
    void User_Failed_By_Null(String userId, String password, String name, String email) {
        Assertions.assertThrows(InvalidRequestFormatException.class,
                () -> new User(userId, password, name, email));
    }
}
