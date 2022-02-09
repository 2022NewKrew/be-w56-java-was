package webserver;

import static org.assertj.core.api.Assertions.*;

import exception.ModelMapException;
import model.User;
import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void addAttribute() {
        User user1 = new User("userId1", "password1", "name1", "test1@test.com");

        Model model = new Model();
        model.addAttribute("user1", user1);

        assertThat(model).hasSize(1).containsOnlyKeys("user1").containsValue(user1);
    }

    @Test
    void addAttribute_null() {
        User user1 = new User("userId1", "password1", "name1", "test1@test.com");

        Model model = new Model();

        assertThatThrownBy(() -> model.addAttribute(null, user1))
                .isInstanceOf(ModelMapException.class)
                .hasMessage("Model 의 key 값은 null 이 될 수 없습니다.");
    }

    @Test
    void getValue() {
        User user1 = new User("userId1", "password1", "name1", "test1@test.com");
        User user2 = new User("userId2", "password2", "name2", "test2@test.com");

        Model model = new Model();
        model.addAttribute("user1", user1);
        model.addAttribute("user2", user2);

        assertThat(model.getValue("user1")).isEqualTo(user1);
    }

    @Test
    void getValue_doesNotContain() {
        User user1 = new User("userId1", "password1", "name1", "test1@test.com");
        User user2 = new User("userId2", "password2", "name2", "test2@test.com");

        Model model = new Model();
        model.addAttribute("user1", user1);
        model.addAttribute("user2", user2);

        assertThatThrownBy(() -> model.getValue("user3"))
                .isInstanceOf(ModelMapException.class)
                .hasMessage("Model 에 key(user3)가 존재하지 않습니다.");
    }
}
