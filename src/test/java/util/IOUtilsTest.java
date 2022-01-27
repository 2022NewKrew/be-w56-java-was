package util;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readRequestLine() throws IOException {
        String httpRequestHeader = "GET /index.html HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive\r\n";
        StringReader sr = new StringReader(httpRequestHeader);

        String requestLine = IOUtils.readRequestLine(new BufferedReader(sr));

        assertThat(requestLine).isEqualTo("GET /index.html HTTP/1.1");
    }

    @Test
    public void readHttpHeaders() throws IOException {
        String httpRequestHeader = "Host: localhost:8080\r\nConnection: keep-alive\r\n";
        StringReader sr = new StringReader(httpRequestHeader);

        List<String> httpHeaders = IOUtils.readHttpHeaders(new BufferedReader(sr));

        assertThat(httpHeaders).contains("Host: localhost:8080").contains("Host: localhost:8080");
    }

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }
}
