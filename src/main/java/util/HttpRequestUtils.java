package util;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class HttpRequestUtils {
    private static final String HEADER_SEPARATOR = ":";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String COOKIE_SEPARATOR = ";";
    private static final String VALUE_SEPARATOR = "=";

    /**
     * @apiNote  queryString은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, PARAMETER_SEPARATOR);
    }

    /**
     * @apiNote 쿠키 값은 name1=value1; name2=value2 형식임
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, COOKIE_SEPARATOR);
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, VALUE_SEPARATOR)).filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue) || Strings.isNullOrEmpty(regex)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0].trim(), tokens[1].trim());
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, HEADER_SEPARATOR);
    }

    public static class Pair {
        private final String key;
        private final String value;

        Pair(String key, String value) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return key.equals(pair.key) && value.equals(pair.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
