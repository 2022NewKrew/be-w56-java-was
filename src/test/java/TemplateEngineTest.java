import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.User;
import org.junit.jupiter.api.Test;
import webserver.view.ModelAndView;
import webserver.view.TemplateEngine;
import static org.assertj.core.api.Assertions.assertThat;

public class TemplateEngineTest {
    @Test
    void renderHtml() throws IOException, IllegalAccessException {
        ModelAndView mv = new ModelAndView();
        TemplateEngine templateEngine = new TemplateEngine();
        mv.setViewName("/user/list.html");
        byte[] body = Files.readAllBytes(new File("./webapp" + mv.getViewName()).toPath());

        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1).name("test1").email("asdf@asdf").build());
        users.add(User.builder().id(2).name("test2").email("asdf2@asdf").build());
        mv.addAttribute("users", users);
        String resultHtml = new String(templateEngine.render(mv, body));

        System.out.println(resultHtml);
        assertThat(resultHtml).contains("<th scope=\"row\"><a href=\"/users/1\" />1</a></th><td>null</td> <td>test1</td> <td>asdf@asdf</td><td><a href=\"/users/1/form\"");
        assertThat(resultHtml).contains("<th scope=\"row\"><a href=\"/users/2\" />2</a></th><td>null</td> <td>test2</td> <td>asdf2@asdf</td><td><a href=\"/users/2/form\"");
    }
}
