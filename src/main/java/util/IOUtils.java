package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static byte[] readBodyFromFile(Map<String, String> requestMap){
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File("./webapp" + requestMap.get("Url")).toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return body;
    }
}
