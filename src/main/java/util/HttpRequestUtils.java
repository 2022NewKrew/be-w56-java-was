package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import domain.Constants;
import domain.ContentType;

public class HttpRequestUtils {

    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, Constants.QUERY_STRING_DELIMITER);
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, Constants.COOKIE_DELIMITER);
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens)
                .map(t -> getKeyValue(t, Constants.PARAMETER_DELIMITER))
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

    public static Map<String, String> parseHeaders(BufferedReader bufferedReader) throws IOException {
        String headers = readHeaders(bufferedReader).toString();

        if (Strings.isNullOrEmpty(headers)) {
            return Maps.newHashMap();
        }

        String[] tokens = headers.split(Constants.LINE_DELIMITER);
        return Arrays.stream(tokens)
                .map(HttpRequestUtils::parseHeader)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static StringBuffer readHeaders(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String readLine;
        while (!(readLine = bufferedReader.readLine()).equals(Constants.EMPTY_STRING)) {
            stringBuffer.append(readLine).append(Constants.LINE_DELIMITER);
        }
        return stringBuffer;
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, Constants.HEADER_DELIMITER);
    }

    public static ContentType parseContentType(String path) {
        String[] strings = path.split(Constants.FILE_EXTENSION_DELIMITER);
        String fileExtension = strings[strings.length-1];
        return ContentType.getIfPresent(fileExtension.toUpperCase());
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
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pair pair = (Pair) o;
            return key.equals(pair.key) && value.equals(pair.value);
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
