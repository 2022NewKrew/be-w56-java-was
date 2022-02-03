package util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParsingUtilsTest {

    @Test
    public void parseNullOrEmpty_ThrowException() {
        assertThatThrownBy(() -> ParsingUtils.parse("", "="))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ParsingUtils.parse(null, "="))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ParsingUtils.parse("", "=", 2))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ParsingUtils.parse(null, "=", 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void parseSuccess() {
        assertThat(ParsingUtils.parse("abc=abc", "="))
                .isEqualTo(new String[]{"abc", "abc"});

        Map<String, String> map = new HashMap<>();
        map.put("abc", "abc");
        map.put("abcd", "abcd");
        assertThat(ParsingUtils.parse("abc=abc; abcd=abcd", "; ", "="))
                .isEqualTo(map);
    }

    @Test
    public void parseSuccess_InvalidPair() {
        Map<String, String> map = new HashMap<>();
        map.put("abc", "abc");
        assertThat(ParsingUtils.parse("abc=abc; abcd", "; ", "="))
                .isEqualTo(map);
    }

    @Test
    public void parseWithInvalidCount_ThrowException() {
        assertThatThrownBy(() -> ParsingUtils.parse("abc=", "=", 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getKeyValue() {
        Pair pair = ParsingUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    public void getKeyValue_invalid() {
        Pair pair = ParsingUtils.getKeyValue("userId", "=");
        assertThat(pair).isNull();
    }
}
