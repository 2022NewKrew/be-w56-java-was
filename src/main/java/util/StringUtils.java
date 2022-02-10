package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static Map<String, String> parseJsonString(String s) {
        Map<String, String> output = new HashMap<>();
        Pattern pattern = Pattern.compile("\"(.*?)\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            output.put(matcher.group(1), matcher.group(2));
        }
        return output;
    }
}
