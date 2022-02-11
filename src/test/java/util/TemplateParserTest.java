package util;

import application.domain.User;
import framework.modelAndView.Model;
import framework.template.TemplateParser;
import framework.template.TemplateParser3;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplateParserTest {

    @Test
    void parser_테스트() {

        String template = "<th scope=\"row\">{{id}}</th> <td><a href=\"/users/{{userId}}\">{{userId}}</a></td> <td>{{name}}</td> <td>{{email}}</td><td><a href=\"/users/{{userId}}/form\" class=\"btn btn-success\" role=\"button\">수정</a></td>";
        Model model = new Model();
        StringBuilder sb = new StringBuilder();

        model.addAttribute("id", "id1");
        model.addAttribute("userId", "userId1");
        model.addAttribute("name", "nam");
        model.addAttribute("email", "emailllll");

        TemplateParser3 templateParser = new TemplateParser3(template, model);

        try {
            templateParser.getTemplateWithModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(sb);
    }



    @Test
    void parser_테스트_List() {

        String template = " {{#users}}\n" +
                "              <tbody>\n" +
                "                <tr>\n" +
                "                    <th scope=\"row\">{{userId}}</th> <td><a href=\"/users/{{userId}}\">{{userId}}</a></td> <td>{{name}}</td> <td>{{email}}</td><td><a href=\"/users/{{userId}}/form\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>\n" +
                "<!--                <tr>-->\n" +
                "<!--                    <th scope=\"row\">2</th> <td>slipp</td> <td>슬립</td> <td>slipp@sample.net</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>-->\n" +
                "<!--                </tr>-->\n" +
                "              </tbody>\n" +
                "              {{/users}}";
        Model model = new Model();
        StringBuilder sb = new StringBuilder();

        List<User> users = new ArrayList<>();

        users.add(new User("testId", "fsd", "test", "test@test.com"));
        users.add(new User("testId2", "fsd2", "test2", "test2@test.com"));

        model.addAttribute("users", users);

        TemplateParser3 templateParser = new TemplateParser3(template, model);

        try {
            templateParser.getTemplateWithModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(sb);
    }
}
