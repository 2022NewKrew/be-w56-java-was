package util;

import java.io.BufferedReader;
import java.io.StringReader;

import com.google.common.hash.BloomFilter;
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
    public void readStartLine() throws Exception {
        String startline = "GET /index.html HTTP/1.1";
        StringReader sr = new StringReader(startline);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse start-line : {}", IOUtils.readStartLine(br));
    }
}
