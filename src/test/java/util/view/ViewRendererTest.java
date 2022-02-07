package util.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.response.ModelAndView;
import webserver.domain.entity.User;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ViewRendererTest {

    @Test
    @DisplayName("for loop가 있는 View Render 성공 확인")
    void successViewRenderWithLoop() throws IOException, NoSuchFieldException, IllegalAccessException {
        // given
        final ModelAndView mav = new ModelAndView("/user/list.html");
        final List<User> users = getUsers();
        mav.addAttribute("users", users);

        //when
        final String rendered = ViewRenderer.getRenderedView(mav);

        //then
        users.forEach(user -> assertContainUser(rendered, user));
    }

    private static void assertContainUser(String rendered, User user){
        assertThat(rendered).contains(user.getUserId());
        assertThat(rendered).contains(user.getName());
        assertThat(rendered).contains(user.getEmail());
    }

    private static List<User> getUsers(){
        return List.of(
                new User("id1", "password1", "name1", "email1"),
                new User("id2", "password2", "name2", "email2"),
                new User("id3", "password3", "name3", "email3"),
                new User("id4", "password4", "name4", "email4")
        );
    }
}