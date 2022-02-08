package http.util;

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    /**
     * @param queryString URL ? 이후에 전달되는 field1=value1&field2=value2 형식
     * @return 파싱된 key-value 쌍 Map
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies name1=value1; name2=value2 형식
     * @return 파싱된 key-value 쌍 Map
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] buf = new char[contentLength];
        br.read(buf, 0, contentLength);
        return String.valueOf(buf);
    }

    /* ---------------------------------------------------------------------- */

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return new HashMap<>();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "="))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Pair {
        private final String key;
        private final String value;

        @Builder
        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }
    }
}
