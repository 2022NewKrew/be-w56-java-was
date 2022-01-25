package util;

public class StringUtils {

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String trim(String value) {
        return value.trim();
    }
}
