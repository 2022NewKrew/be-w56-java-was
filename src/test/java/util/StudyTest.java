package util;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class StudyTest {

    @Test
    public void defaultCharset() {
        System.out.println(Charset.defaultCharset().name());
    }
}
