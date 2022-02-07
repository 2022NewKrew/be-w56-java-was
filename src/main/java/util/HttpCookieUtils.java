package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static http.HttpCookie.*;

public class HttpCookieUtils {
    public static String START_DELIMITER = "Cookie: ";
    public static String ATTR_DELIMITER = "; ";
    public static String KEY_VALUE_DELIMITER = "=";

    public static Map<String, String> parseCookie(List<String> requestHeader) {
        Map<String, String> attribute = new HashMap<>();

        //parse cookies from requestHeader
        for (String lineStr : requestHeader) {
            if (lineStr.startsWith(START_DELIMITER)) {
                String cookieStr = lineStr.substring(START_DELIMITER.length(), lineStr.length());
                Arrays.asList(cookieStr.split(ATTR_DELIMITER)).stream().forEach(
                        (keyValueStr) -> {
                            String[] keyValue = keyValueStr.split(KEY_VALUE_DELIMITER);
                            attribute.put(keyValue[0], keyValue[1]);
                        });

                break;
            }
        }

        return attribute;
    }
}
