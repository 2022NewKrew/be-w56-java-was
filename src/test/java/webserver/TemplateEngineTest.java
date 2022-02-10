package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import model.User;
import org.junit.jupiter.api.Test;

class TemplateEngineTest {

    private static final String before = ""
        + "{{#users}}"
        + "<tr>{{userId}}{{name}}{{email}}</tr>"
        + "{{/users}}"
        + "{{#users}}"
        + "<tr>{{userId}}{{name}}{{email}}</tr>"
        + "{{/users}}";
    private static final String after = "<tr>123</tr><tr>456</tr><tr>123</tr><tr>456</tr>";
    private static final Model model = new Model();

    static {
        User user1 = User.builder().userId("1").name("2").email("3").build();
        User user2 = User.builder().userId("4").name("5").email("6").build();
        model.setAttribute("users", List.of(user1, user2));
    }

    @Test
    void render() {
        String render = TemplateEngine.render(before, model);
        assertThat(render).isEqualTo(after);
    }
}