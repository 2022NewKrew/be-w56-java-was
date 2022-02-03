package util;

import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParsingUtils {

    private ParsingUtils() {
    }

    public static String[] parse(String string, String delimiter) {
        validateNull(string);
        return string.split(delimiter);
    }

    public static String[] parse(String string, String delimiter, int count) {
        validateNull(string);
        String[] tokens = string.split(delimiter);
        if (tokens.length != count) {
            throw new IllegalArgumentException();
        }
        return tokens;
    }

    public static Map<String, String> parse(String string, String delimiter, String regex) {
        String[] tokens = parse(string, delimiter);
        return Arrays.stream(tokens)
                .map(token -> ParsingUtils.getKeyValue(token, regex))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static void validateNull(String string) {
        if (Strings.isNullOrEmpty(string)) {
            throw new IllegalArgumentException();
        }
    }

    public static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }
        return new Pair(tokens[0], tokens[1]);
    }
}
