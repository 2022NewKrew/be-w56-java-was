package util;

import DTO.ModelAndView;
import model.Model;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.DynamicHtmlParsingUtils.*;

class HtmlParserTest {
    private static final Logger log = LoggerFactory.getLogger(HtmlParserTest.class);

    private String removeWhiteSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
    @Test
    void fillDynamicHtml() {
    }

    @Test
    void getListSectionTest() {
        ModelAndView mav = new ModelAndView("testview");
        Model user1 = new User("user1","pw1","carrot","carrot@kakao.corp");
        Model user2 = new User("user2","pw2","apple","apple@kakao.corp");
        Model user3 = new User("user3","pw3","banana","banana@kakao.corp");
        List<Model> users = Arrays.asList(user1, user2, user3);
        mav.addObject("users", users);

        String body = "<head>\n" + "<tbody>\n" +
                "                {{#users}}\n" +
                "                <tr>\n" +
                "                    <td><a href=\"/user/profile/{{userId}}\" />{{userId}}</a></td> <td>{{name}}</td><td>2021-12-14</td>\n" +
                "                </tr>\n" +
                "                {{/users}}\n" +
                "                </tbody>";

        String bodyAns = "<head>\n" + "<tbody>\n" +
                "                <tr>\n" +
                "                    <td><a href=\"/user/profile/{{userId}}\" />user1</a></td> <td>carrot</td><td>2021-12-14</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td><a href=\"/user/profile/{{userId}}\" />user2</a></td> <td>apple</td><td>2021-12-14</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td><a href=\"/user/profile/{{userId}}\" />user3</a></td> <td>banana</td><td>2021-12-14</td>\n" +
                "                </tr>\n" +
                "                </tbody>";

        String replaced = fillListSections(body, mav);
        assertEquals(removeWhiteSpaces(replaced), removeWhiteSpaces(bodyAns));
    }

    @Test
    @DisplayName("한 model 당 가진 Parameter 조회 후 해당하는 string 값 변경 ")
    void fillelemTest() {
        String body =
                "                <tr>\n" +
                "                    <td><a href=\"/user/profile/{{userId}}\" />{{userId}}</a></td> <td>{{name}}</td><td>2021-12-14</td>\n" +
                "                </tr>\n";
        System.out.println(body);
        Model user = new User("user1","pw1","carrot","carrot@kakao.corp");
        String replaced = fillElement(body, user);

        String bodyAns =
                "                <tr>\n" +
                        "                    <td><a href=\"/user/profile/{{userId}}\" />user1</a></td> <td>carrot</td><td>2021-12-14</td>\n" +
                        "                </tr>\n";
        System.out.println("-------- changed to ------");
        System.out.println(replaced);
        assertEquals(replaced, bodyAns);
    }

    @Test
    @DisplayName("주어진 parameter가 model에 없는 경우 읽지 않음")
    void fillelemTest2() {
        String body =
                "                <tr>\n" +
                "                    <td><a href=\"/user/profile/{{userId}}\" />{{phoneNumber}}</a></td> <td>{{name}}</td><td>2021-12-14</td>\n" +
                "                </tr>\n";

        String bodyAns =
                "                <tr>\n" +
                        "                    <td><a href=\"/user/profile/{{userId}}\" />{{phoneNumber}}</a></td> <td>carrot</td><td>2021-12-14</td>\n" +
                        "                </tr>\n";
        System.out.println(body);
        Model user = new User("user1","pw1","carrot","carrot@kakao.corp");
        String replaced = fillElement(body, user);
        System.out.println("-------- changed to ------");
        System.out.println(replaced);

        assertEquals(replaced, bodyAns);
    }
}
