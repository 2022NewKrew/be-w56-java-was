package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    public void readRequest() throws IOException {
        String request = "GET /index.html HTTP/1.1";
        StringReader sr = new StringReader(request);
        BufferedReader br = new BufferedReader(sr);

        Map<String, String> requestMap = IOUtils.readRequest(br);

        logger.debug("requestMap : {}", requestMap);
    }

    @Test
    public void readHeader() throws IOException {
        String header = "Accept: text/css,*/*;q=0.1\n" + "Connection: keep-alive\n";
        StringReader sr = new StringReader(header);
        BufferedReader br = new BufferedReader(sr);

        Map<String, String> headerMap = IOUtils.readHeader(br);

        logger.debug("headerMap : {}", headerMap);
    }
}
