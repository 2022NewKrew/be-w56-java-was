package webserver.common.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import webserver.dto.request.HttpRequestStartLine;

import java.util.*;
import java.util.stream.Collectors;

public class HttpUtils {
    /**
     * @param queryString 은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static HttpRequestStartLine parseStartLine(String line) {
        if (Strings.isNullOrEmpty(line)) {
            return null;
        }

        boolean hasQueryString = false;
        if (line.contains("?")) {
            line = line.replace("?", " ");
            hasQueryString = true;
        }
        String[] parsed = line.split(" ");
        Map<String, String> queryParameters = hasQueryString ? parseQueryString(parsed[2]) : Collections.emptyMap();
        return HttpRequestStartLine.valueOf(parsed[0], parsed[1], parsed[parsed.length - 1], queryParameters);
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens)
                .map(t -> getKeyValue(t, "="))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
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

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static List<String> mappedHeaderToList(Map<String, String> mappedHeader) {
        List<String> header = new ArrayList<>();
        mappedHeader.forEach((key, value) -> header.add(key + ": " + value + "\r\n"));
        return header;
    }

    @Getter
    @AllArgsConstructor
    public static class Pair {
        String key;
        String value;

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
