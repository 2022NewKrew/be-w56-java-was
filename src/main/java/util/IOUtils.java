package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final String PROP_SEPERATOR = "&";
    private static final String PROP_DEFINE = "=";

    private IOUtils() {}

    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static byte[] readFile(String filePath) {
        byte[] body = new byte[0];
        try {
            body = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return body;
    }

    public static Map<String, String> getBodyData(String body) {
        Map<String, String> props = new HashMap<>();
        for (String prop: body.split(PROP_SEPERATOR)) {
            putProp(props, prop);
        }
        return props;
    }

    private static void putProp(Map<String, String> props, String prop) {
        if (prop.contains(PROP_DEFINE)) {
            props.put(prop.split(PROP_DEFINE)[0], prop.split(PROP_DEFINE)[1]);
        }
    }
}
