package controller.request;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오전 11:50
 */
class RequestHeaderTest {
    @Test
    public void requestHeader() {
        // given
        List<String> headerStrings = List.of("Host: test", "Content-Type: test");

        // when
        RequestHeader requestHeader = RequestHeader.of(headerStrings);

        // then
        assertThat(requestHeader.getParameter("Host")).isEqualTo("test");
        assertThat(requestHeader.getParameter("Content-Type")).isEqualTo("test");
    }

    @Test
    public void requestHeaderNull() {
        // given
        List<String> headerStrings = List.of("Host: test", "Content-Type: test");

        // when
        RequestHeader requestHeader = RequestHeader.of(headerStrings);

        // then
        assertThat(requestHeader.getParameter("Content-Length")).isNull();
    }
}
