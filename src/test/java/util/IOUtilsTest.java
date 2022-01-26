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
    public void getRequestLines() throws IOException {
        String httpRequestHeader = "GET /index.html HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive\r\n";
        StringReader sr = new StringReader(httpRequestHeader);

        List<String> requestLines = IOUtils.getRequestLines(new BufferedReader(sr));

        assertThat(requestLines).hasSize(3);
    }

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }
}
