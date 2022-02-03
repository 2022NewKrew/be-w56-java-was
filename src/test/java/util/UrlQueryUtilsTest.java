package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class UrlQueryUtilsTest {
    @DisplayName("url 파라메터 분리 후 Map으로 만드는 과정 테스트")
    @Test
    void parseUrlQuery() {
        Map<String, String> urlParameterMap = UrlQueryUtils.parseUrlQuery("aaa=aaa&bbb=bbb&ccc");

        assertThat(urlParameterMap.get("aaa")).isEqualTo("aaa");
        assertThat(urlParameterMap.size()).isEqualTo(2);
        assertThat(urlParameterMap.get("ccc")).isEqualTo(null);
    }
}
