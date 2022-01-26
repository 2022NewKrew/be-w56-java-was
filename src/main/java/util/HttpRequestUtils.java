package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    /**
     * @param queryString URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

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

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static Map<String, String> parseReponseLine(String requestLine) {
        Map<String, String> ret = new HashMap<>();
        String[] parsed = requestLine.split(" ");
        String[] urlParsed = parsed[1].split("\\?");
        if (urlParsed.length == 2) {
            ret.put("query", urlParsed[1]);
        }
        ret.put("method", parsed[0]);
        ret.put("url", urlParsed[0]);
        ret.put("protocol", parsed[2]);

        return ret;
    }

    public static Map<String, String> parseResponseHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        do {
            line = br.readLine();
            Pair header = parseHeader(line);
            if (header != null)
                headers.put(header.getKey(), header.getValue());
        } while (!line.equals(""));

        return headers;
    }

    public static byte[] matchURL(String requestURL) throws IOException {
        File file = new File("./webapp" + requestURL);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }
        return Files.readAllBytes(new File("./webapp/404.html").toPath());
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
            } else return value.equals(other.value);
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
