package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] charBuf = new char[contentLength];
        br.read(charBuf, 0, contentLength);
        String body = new String(charBuf);
        return URLDecoder.decode(body, StandardCharsets.UTF_8);
    }
}
