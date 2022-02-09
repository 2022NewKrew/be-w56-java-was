package webserver;

import static org.assertj.core.api.Assertions.*;

import dto.UserProfileDto;
import exception.TemplateSyntaxException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MyTemplateViewTest {

    private static final Logger log = LoggerFactory.getLogger(MyTemplateViewTest.class);

    @Test
    void renderTemplateModel() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<UserProfileDto> users = new ArrayList<>();
        User user1 = new User("userId1", "password1", "name1", "test1@test.com");
        users.add(UserProfileDto.from(user1));
        User user2 = new User("userId2", "password2", "name2", "test2@test.com");
        users.add(UserProfileDto.from(user2));

        Model model = new Model();
        model.addAttribute("users", users);

        String template = new String(MyTemplateView.getInstance().renderTemplateModel("/users/list.html", model));
        log.debug(template);
        assertThat(template).contains("userId1").contains("userId2").doesNotContain("{{#users}}");
    }

    @Test
    void renderTemplateModel_noEndTage() {
        List<UserProfileDto> users = new ArrayList<>();
        User user1 = new User("userId1", "password1", "name1", "test1@test.com");
        users.add(UserProfileDto.from(user1));

        Model model = new Model();
        model.addAttribute("users", users);

        assertThatThrownBy(() -> MyTemplateView.getInstance().renderTemplateModel("/test/template_view_test.html", model))
                .isInstanceOf(TemplateSyntaxException.class)
                .hasMessage("end tag 를 찾을 수 없습니다.");
    }
}
