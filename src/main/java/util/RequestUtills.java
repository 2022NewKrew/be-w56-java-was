package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestUtills {
    private static final Logger log = LoggerFactory.getLogger(RequestUtills.class);

    public static String readUrlPath(BufferedReader br) throws IOException {
        String line = br.readLine();
        String requestUrl = line.split(" ")[1];
        log.info("REQ : " + requestUrl);
        return requestUrl;
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        String line;
        while ((line = br.readLine()) != null) {
            log.info("request header " + line);
            if (line.equals("")) {
                break;
            }
            String key = line.split(":")[0];
            String value = line.split(":")[1].trim();
            map.put(key, value);
        }
        return map;
    }
}
