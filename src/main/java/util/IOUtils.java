package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
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
        return URLDecoder.decode(String.copyValueOf(body), "UTF-8");
    }

    public static Map<String, String> readRequest(BufferedReader br) throws IOException {
        String request = br.readLine();
        return HttpRequestUtils.parseRequest(request);
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        String line;
        Map<String, String> headerMap = new HashMap<>();
        HttpRequestUtils.Pair pair;
        while(true) {
            line = br.readLine();
            if(line == null || line.equals(""))
                return headerMap;
            pair = HttpRequestUtils.parseHeader(line);
            headerMap.put(pair.getKey(), pair.getValue());
        }
    }
}
