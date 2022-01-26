package util.libtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class regexTest {

    @Test
    @DisplayName("정적 리소스 정규식 테스트")
    void regexTest() {
        String pattern = "^[a-zA-Z0-9/]*\\.[a-z]*$";
        String target1 = "/index.html";

        Assertions.assertEquals(Pattern.matches(pattern, target1), true);
    }
}
