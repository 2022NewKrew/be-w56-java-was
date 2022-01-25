package common;

public class ObjectUtil {

    private static final int INT_ZERO = 0;

    private ObjectUtil() {

    }

    public static String checkNonEmptyAfterTrim(final String value, final String name) {
        String trimmed = checkNotNull(value, name).trim();
        return checkNonEmpty(trimmed, name);
    }

    public static <T> T checkNotNull(final T arg, final String text) {
        if(arg == null) {
            throw new NullPointerException(text);
        }
        return arg;
    }

    public static String checkNonEmpty(final String value, final String name) {
        if(checkNotNull(value, name).isEmpty()) {
            throw new IllegalArgumentException("Param '" + name + "' must not be empty");
        }
        return value;
    }

    public static int checkPositiveOrZero(int i, String name) {
        if ( i < INT_ZERO) {
            throw new IllegalArgumentException(name + " : " + i + " (expected: >= 0)");
        }
        return i;
    }
}
