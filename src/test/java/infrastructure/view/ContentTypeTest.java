package infrastructure.view;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentTypeTest {

    @Test
    @DisplayName("contenttype check")
    void test() throws Exception {
        // given

        String str1 = "index.html";
        String[] split = str1.split("\\.");
        for (String s : split) {
            System.out.println("s = " + s);
        }

        System.out.println("split[split.length] = " + split[split.length-1]);

        // when

        // then

    }

}
