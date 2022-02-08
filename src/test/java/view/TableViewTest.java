package view;

import model.Model;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TableViewTest {

    @Test
    void tableFrameTBodyTest() {
        UserInfo info1 = new UserInfo("peach1", "peach1@kakao.com", "a");
        UserInfo info2 = new UserInfo("peach2", "peach2@kakao.com", "b");
        UserInfo info3 = new UserInfo("peach3", "peach3@kakao.com", "c");
        List<UserInfo> userInfos = Arrays.asList(info1, info2, info3);
        Model model = Model.create();
        model.addAttribute("userlist", userInfos);

        String tbody = TableComponentFrame.tbody(model.getAttribute("userlist"), "userId", "email", "name");

        System.out.println(tbody);
        Assertions.assertThat(tbody).isNotEqualTo(" ");
    }

    @Test
    void tableFrameTHeadTest() {

        String thead = TableComponentFrame.thead("#", "아이디", "이메일", "이름", " ");

        System.out.println(thead);
        Assertions.assertThat(thead).isNotEqualTo(" ");
    }
}
