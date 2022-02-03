package util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParsingUtilsTest {

    @Disabled
    @Test
    public void parseCookies() {
//        String cookies = "logined=true; JSessionId=1234";
//        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
//        assertThat(parameters.get("logined")).isEqualTo("true");
//        assertThat(parameters.get("JSessionId")).isEqualTo("1234");
//        assertThat(parameters.get("session")).isNull();
    }

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

        assertThat(ParsingUtils.parse("abc=abc; abcd=abcd; ", "; "))
                .isEqualTo(new String[]{"abc=abc", "abcd=abcd"});
    }

    @Test
    public void parseWithInvalidCount_ThrowException() {
        assertThatThrownBy(() -> ParsingUtils.parse("abc=", "="))
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
