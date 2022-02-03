package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestHeaderTest {
    @DisplayName("Header 생성 테스트")
    @Test
    void makeHttpRequestHeader() {
        List<String> headerList = Arrays.asList("aa:aa", "bb:bb", "cc");

        HttpRequestHeader httpRequestHeader = HttpRequestHeader.makeHttpRequestHeader(headerList);

        assertThat(httpRequestHeader.getHeaderMap().size()).isEqualTo(2);
        assertThat(httpRequestHeader.getHeaderMap().get("aa")).isEqualTo("aa");
        assertThat(httpRequestHeader.getHeaderMap().get("bb")).isEqualTo("bb");
        assertThat(httpRequestHeader.getHeaderMap().get("cc")).isEqualTo(null);
    }
}
