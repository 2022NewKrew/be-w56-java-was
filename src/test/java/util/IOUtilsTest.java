package util;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

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
    public void getContentType() throws Exception {
        File file1 = new File("./webapp/index.html");
        assertEquals(IOUtils.getContentType(file1),"text/html");

        File file2 = new File("./webapp/css/styles.css");
        assertEquals(IOUtils.getContentType(file2),"text/css");
    }
}
