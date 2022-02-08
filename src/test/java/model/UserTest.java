package model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User 테스트")
class UserTest {

    @DisplayName("올바른 파라미터로 User를 생성했을 때 예외를 던지지 않는다.")
    @Test
    void constructor() {
        //give
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        //when
        //then
        assertThatCode(() -> new User(userId, password, name, email)).doesNotThrowAnyException();
    }

    @DisplayName("userId가 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor1() {
        //give
        String userId = null;
        String password = "password";
        String name = "name";
        String email = "email";

        //when
        //then
        assertThatThrownBy(() -> new User(userId, password, name, email)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("password가 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor2() {
        //give
        String userId = "userId";
        String password = null;
        String name = "name";
        String email = "email";

        //when
        //then
        assertThatThrownBy(() -> new User(userId, password, name, email)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("name이 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor3() {
        //give
        String userId = "userId";
        String password = "password";
        String name = null;
        String email = "email";

        //when
        //then
        assertThatThrownBy(() -> new User(userId, password, name, email)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("email이 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor4() {
        //give
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = null;

        //when
        //then
        assertThatThrownBy(() -> new User(userId, password, name, email)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
