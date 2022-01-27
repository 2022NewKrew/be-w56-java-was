package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
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

    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        Map<String, String> headers = new HashMap<>();
        while (line != null && !line.equals("")) {
            headers.put(line.split(":")[0].trim(), line.split(":")[1].trim());
            line = br.readLine();
        }
        return headers;
    }
}
