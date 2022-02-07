package infrastructure.util;

import infrastructure.model.Model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlTemplateUtilsTest {

//    @Test
//    void getFile() throws IOException {
//        String file = HtmlTemplateUtils.getFile("/user/list.html", Pair.of("name", "ijin"), Pair.of("userId", "483759"));
//        assertThat(file).isEqualTo("Hello");
//    }

    @DisplayName("")
    @Test
    void getView() throws IOException {
        // given
        List<Model> lists = List.of(
                new Model(Map.of("userId", "483759", "name", "윤이진", "email", "483759@naver.com")),
                new Model(Map.of("userId", "userId", "name", "name", "email", "email@email.com"))
        );
        String file = HtmlTemplateUtils.getView("/user/list.html", "users", lists);

        assertThat(file).isEqualTo(true);
    }
}