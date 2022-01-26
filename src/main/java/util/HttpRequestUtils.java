package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import webserver.header.ContentType;
import webserver.header.RequestLine;

public final class HttpRequestUtils {

    private HttpRequestUtils() { }

    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static Map<String, String> parseBody(String body) {
        return parseValues(body, "&");
    }

    public static String[] parseGetRequest(String request) {
        return request.split("\\?");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }
        values = URLDecoder.decode(values, StandardCharsets.UTF_8);

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(Objects::nonNull)
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

    public static ContentType parseExtension(String path) {
        String[] pathParsed = path.split("\\.");
        String extension = pathParsed[pathParsed.length - 1];
        return ContentType.matching(extension);
    }

    public static List<Pair> parseRequestLine(String header) {
        String[] parsed = header.split(" ");
        List<Pair> pairs = new ArrayList<>();
        if (parsed.length == 3) {
            pairs.add(new Pair(RequestLine.METHOD.name(), parsed[0]));
            pairs.add(new Pair(RequestLine.PATH.name(), parsed[1]));
            pairs.add(new Pair(RequestLine.VERSION.name(), parsed[2]));
        }
        return pairs;
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
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
                return other.value == null;
            } else {
                return value.equals(other.value);
            }
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
