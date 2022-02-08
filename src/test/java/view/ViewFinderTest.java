package view;

import model.Model;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ViewFinderTest {

    @Test
    void findTest() throws IOException {
        UserInfo info1 = new UserInfo("peach1", "peach1@kakao.com", "a");
        UserInfo info2 = new UserInfo("peach2", "peach2@kakao.com", "b");
        UserInfo info3 = new UserInfo("peach3", "peach3@kakao.com", "c");
        List<UserInfo> userInfos = Arrays.asList(info1, info2, info3);
        Model model = Model.create();
        model.addAttribute("userList", userInfos);

        byte[] bytes = ViewFinder.find("/userList", model);

        Assertions.assertThat(bytes.length).isGreaterThan(0);
    }
}
