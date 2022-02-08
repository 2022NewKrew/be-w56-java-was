package webserver.framework.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webserver.framwork.core.Model;
import webserver.framwork.core.templateengine.TemplateEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TemplateEngineTest {

    @Test
    void mustacheRenderTest(){

        try {
            //given
            List<String> rawFile = Files.readAllLines(new File("./webapp/test/test.html").toPath());
            Model model = new Model();
            model.addAttribute("isOk", true);
            List<User> users = new ArrayList<>();
            users.add(new User("Ethan", 25));
            users.add(new User("Jack", 24));
            users.add(new User("John", 23));
            model.addAttribute("users", users);

            //when
            String result = TemplateEngine.render(rawFile, model);

            //then
            assertThat(result).contains("isOk");
            assertThat(result).contains("Ethan");
            assertThat(result).contains("Jack");


        }catch(IOException e){
            fail("파일 가져오기 실패");
        }
    }

    static class User{
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}
