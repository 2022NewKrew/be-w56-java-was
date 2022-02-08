package util;

import model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HttpTemplateUtilsTest {

    @Test
    void createUserListView() {
        final List<User> users = List.of(new User("javajigi", "password", "자바지기", "javajigi@sample.net"));
        final String result = HttpTemplateUtils.createUserListView(users);
        final String expected = "<tr>\n<th scope=\"row\">1</th> <td>javajigi</td> <td>자바지기</td> <td>javajigi@sample.net</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n</tr>\n";
        assertThat(result).isEqualTo(expected);
    }
}
