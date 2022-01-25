package util;

import org.junit.jupiter.api.Test;
import webserver.request.RequestInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpServletRequestUtilsTest {

    @Test
    void parseRequestLine() {
        String requestLine = "GET /index.html HTTP/1.1";
        RequestInfo requestInfo = HttpServletRequestUtils.parseRequestLine(requestLine);
        assertThat(requestInfo).isEqualTo(new RequestInfo("GET","/index.html","HTTP/1.1"));

    }

    @Test
    void readHeader() throws IOException {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine()).
                thenReturn("Host: localhost:8080").
                thenReturn("Connection: keep-alive").
                thenReturn("Cookie: JSESSIONID=ED5FD4CA35D8A37FF1D0CF498FF2E89F").
                thenReturn("");
        Map<String, String> headerMap = HttpServletRequestUtils.readHeader(bufferedReader);

        assertThat(headerMap.get("Host")).isEqualTo("localhost:8080");
        assertThat(headerMap.get("Connection")).isEqualTo("keep-alive");
        assertThat(headerMap.get("Cookie")).isEqualTo("JSESSIONID=ED5FD4CA35D8A37FF1D0CF498FF2E89F");
        assertThat(headerMap.size()).isEqualTo(3);
    }


}