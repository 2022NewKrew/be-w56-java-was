import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.User;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils;
import webserver.ModelAndView;
import webserver.Response;
import webserver.ViewResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverTest {
    @Test
    public void renderHtml() throws IOException, IllegalAccessException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user/list.html");
        ViewResolver viewResolver = new ViewResolver();
        byte[] body = Files.readAllBytes(new File("./webapp" + mv.getViewName()).toPath());
        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1).name("test1").email("asdf@asdf").build());
        users.add(User.builder().id(2).name("test2").email("asdf2@asdf").build());
        mv.addAttribute("users", users);
        System.out.println(users);
        viewResolver.render(mv, body);
        System.out.println(users.getClass().getName());
    }
}
