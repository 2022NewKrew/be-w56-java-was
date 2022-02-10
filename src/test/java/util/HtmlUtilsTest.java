package util;

import model.Memo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HtmlUtilsTest {

    @Test
    void testMakeIterBlock() {
        try {
            // Given
            List<Memo> memos = new ArrayList<>();
            String name1 = "jy1";
            String name2 = "jy2";
            String content = "hihi";
            LocalDateTime now = LocalDateTime.now();
            memos.add(new Memo(name1, content, now));
            memos.add(new Memo(name2, content, now));
            String view =
                    "<tr>\n" +
                    "   <th scope=\"row\">{{date}}</th> <td>{{name}}</td> <td>{{content}}</td>\n" +
                    "</tr>";

            // When
            StringBuilder sb = HtmlUtils.makeIterBlock(memos, view);

            // then
            String expect = String.format(
                    "<tr>\n" +
                    "   <th scope=\"row\">%s</th> <td>%s</td> <td>%s</td>\n" +
                    "</tr>\n"+
                    "<tr>\n" +
                    "   <th scope=\"row\">%s</th> <td>%s</td> <td>%s</td>\n" +
                    "</tr>\n",
                    now, name1, content, now, name2, content);
            assertThat(sb.toString()).isEqualTo(expect);
        } catch (Exception e) {

        }
    }
    @Test
    void testFillBlock() {
        try {
            // Given
            String name = "jy";
            String content = "hihi";
            LocalDateTime now = LocalDateTime.now();
            Memo memo = new Memo(name, content, now);
            String view =
                    "<tr>\n" +
                    "   <th scope=\"row\">{{date}}</th> <td>{{name}}</td> <td>{{content}}</td>\n" +
                    "</tr>";

            // When
            StringBuilder sb = HtmlUtils.fillBlock(memo, view);

            // then
            String expect = String.format(
                    "<tr>\n" +
                    "   <th scope=\"row\">%s</th> <td>%s</td> <td>%s</td>\n" +
                    "</tr>",
            now, name, content);
            assertThat(sb.toString()).isEqualTo(expect);
        } catch (Exception e) {

        }
    }

}
