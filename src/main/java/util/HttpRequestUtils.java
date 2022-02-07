package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.RequestHeader;
import model.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    /**
     * @param queryString은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static RequestLine parseRequestLine(String url) {
        log.debug("request line : {}", url);
        String[] split = url.split(" ");
        String[] pathSplit = split[1].split("\\?");
        if (pathSplit.length == 1) {
            return new RequestLine(split[0], split[1], split[2]);
        }
        return new RequestLine(split[0], pathSplit[0], pathSplit[1], split[2]);
    }

    public static RequestHeader parseRequestHeader(BufferedReader br) throws IOException {
        final String defaultCookie = "logined=false";
        String line;
        Map<String, String> header = new HashMap<>();
        while (!(line = br.readLine()).equals("")) {
            log.debug("header : {}", line);
            String[] split = line.split(":", 2);
            header.put(split[0].trim(), split[1].trim());
        }

        int contentLength = Integer.parseInt(Optional.ofNullable(header.get("Content-Length")).orElse("0"));
        String contentType = Optional.ofNullable(header.get("Accept")).orElse("").split(",")[0];
        boolean cookie = Boolean.parseBoolean(Optional.ofNullable(header.get("Cookie")).orElse(defaultCookie).split("=")[1]);
        return new RequestHeader(contentLength, contentType, cookie);
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
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
