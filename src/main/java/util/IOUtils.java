package util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param br
     *            Request Body 를 시작하는 시점이어야
     * @param contentLength
     *            Request Header 의 Content-Length 값이다.
     * @throws IOException : IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        int result = br.read(body, 0, contentLength);
        if (result == -1) {
            return null;
        }
        return String.copyValueOf(body);
    }
}
