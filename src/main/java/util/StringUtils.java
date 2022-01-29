package util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String trim(String value) {
        return value.trim();
    }

    public static List<String> parseString(String value, String delimiter) {
        String[] values = value.split(delimiter);
        return Arrays.stream(values).collect(Collectors.toList());
    }
}
