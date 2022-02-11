package util;

public class ObjectUtil {

    private static final int INT_ZERO = 0;

    private ObjectUtil() {

    }

    public static <T> T checkNotNull(final T arg, final String text) {
        if(arg == null) {
            throw new NullPointerException(text);
        }
        return arg;
    }
}
