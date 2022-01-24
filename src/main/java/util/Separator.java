package util;

import java.util.Arrays;
import java.util.Objects;

public final class Separator {
    public static final String DEFAULT_SEPARATOR = ",";

    private Separator() {}

    public static String[] splitString(final String str) {
        return splitString(str, DEFAULT_SEPARATOR);
    }

    public static String[] splitString(final String str, final String regex) {
        return Arrays.stream(
                (Objects.requireNonNull(str) + " ")
                        .split(Objects.requireNonNull(regex))
                )
                .map(String::trim)
                .toArray(String[]::new);
    }
}
