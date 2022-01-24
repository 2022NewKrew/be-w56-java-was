package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import webserver.HttpMethod;

public class HttpRequestUtils {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String PATH_QUERY_STRING_DELIMITER = "\\?";
    private static final String QUERY_DELIMITER = "&";
    private static final String COOKIE_DELIMITER = ";";
    private static final String ACCEPT_DELIMITER = ",";
    private static final String PARAMETER_KEY_VALUE_DELIMITER = "=";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";

    private HttpRequestUtils() {
    }

    public static String[] parseRequestLine(List<String> requestLines) {
        return requestLines.get(0).split(REQUEST_LINE_DELIMITER);
    }

    public static HttpMethod parseHttpMethod(String methodToken) {
        return HttpMethod.valueOf(methodToken);
    }

    public static String parsePath(String targetToken) {
        return targetToken.split(PATH_QUERY_STRING_DELIMITER)[0];
    }

    /**
     * @param queryString은
     *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueries(String targetToken) {
        if (!targetToken.contains(PATH_QUERY_STRING_DELIMITER)) {
            return new HashMap<>();
        }
        String queryString = targetToken.split(PATH_QUERY_STRING_DELIMITER)[1];
        return parseValues(queryString, QUERY_DELIMITER);
    }

    public static String parseAccepts(String accepts) {
        return accepts.split(ACCEPT_DELIMITER)[0];
    }

    /**
     * @param 쿠키
     *            값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, COOKIE_DELIMITER);
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens)
                .map(t -> getKeyValue(t, PARAMETER_KEY_VALUE_DELIMITER))
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

    public static Map<String, String> parseHeaders(List<String> headers) {
        return headers.stream()
                .map(HttpRequestUtils::parseHeader)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, HEADER_KEY_VALUE_DELIMITER);
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
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
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
