package util.request;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    public static String readLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.info("request line {}: ", line);
        return line;
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line = br.readLine();
        while (!Strings.isNullOrEmpty(line)) {
            String[] split = line.split(": ");
            headers.put(split[0], split[1]);
            log.debug("request header {} : {}", split[0], split[1]);
            line = br.readLine();
        }

        return headers;
    }

    public static String readBody(BufferedReader br, int contentLength) throws IOException{
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.valueOf(body);
    }
}
