package util.util;

import app.db.DataBase;
import app.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import util.TemplateEngine;
import util.ui.Model;
import util.ui.ModelImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TemplateEngineTest {

    @BeforeAll
    void setUp(){
        DataBase.addUser(new User("yunyul", "test", "윤렬", "yunyul3@gmail.com"));
        DataBase.addUser(new User("tester", "test", "테스터", "test@test.com"));
    }

    @Test
    void renderModel() throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Model model = new ModelImpl();
        model.addAttribute("hello", "world");
        String template = "{{hello}}\n";
        assertThat(TemplateEngine.divideByList(template, model).toString()).isEqualTo("world\n");
    }

    // Todo 객체 하나만 넣어서 {{user.userId}} 이런 식으로 랜더링 하는 것은 아직 안됨... 되게 해야하나?
    @Test
    void renderObject() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Model model = new ModelImpl();
        User user = DataBase.findUserById("yunyul");
        model.addAttribute("user", user);
        String template = "{{userId}}\n";
        assertThat(TemplateEngine.divideByList(template, model).toString()).isEqualTo("yunyul\n");

    }

    @Test
    void renderList() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Model model = new ModelImpl();
        model.addAttribute("users", Arrays.asList(DataBase.findAll().toArray()));
        String template = "{{#users}}" +
                "{{userId}}\n" +
                "{{/users}}";
        String res = TemplateEngine.divideByList(template, model).toString();
        assertThat(res).contains("yunyul", "tester");
        System.out.println(res);
    }



}
