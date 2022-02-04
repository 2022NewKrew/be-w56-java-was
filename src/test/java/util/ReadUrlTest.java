package util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class ReadUrlTest {

    private static final Logger log = LoggerFactory.getLogger(ReadUrlTest.class);

    @Test
    public void testGETMethod(){
        String line = "GET /index.html HTTP/1.1";
        String url = ReadUrl.parseUrl(line, "GET");
        assert(url.equals("/index.html"));
    }

    @Test
    public void openURLTest(){
        String line = "/index.html";
        byte[] openFile =  ReadUrl.openUrl(line);
        log.debug("openFile {}", openFile);
        assert(openFile != null);
    }

}
