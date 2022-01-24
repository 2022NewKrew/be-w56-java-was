package util;

import org.apache.tika.Tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class IOUtils {
    /**
     * @param br
     *            Request Body를 시작하는 시점이어야
     * @param contentLength
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String readMimeType(String url) throws IOException {
        Tika tika = new Tika();
        return tika.detect(new File(url));
    }

}
