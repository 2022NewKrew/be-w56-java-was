package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.request.RequestBody;
import webserver.request.RequestHeader;
import webserver.request.RequestLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);
    private static final String EMPTY = "";
    private static final String DOUBLE_DOT = " ";

    public static Request createRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        if(line == null || line.equals(EMPTY)) {
            return null;
        }
        RequestLine requestLine = RequestLine.from(line);
        log.info("requestLine: {}", requestLine);

        RequestHeader requestHeader = createRequestHeader(br);
        log.info("requestHeader: {}", requestHeader);

        RequestBody requestBody = null;
        if(requestHeader.getHeaders().containsKey("Content-Length")) {
            requestBody = RequestBody.from(IOUtils.readData(br, Integer.valueOf(requestHeader.getHeaders().get("Content-Length"))));
        }

        log.info("requestBody: {}", requestBody);
        return Request.of(requestLine, requestHeader, requestBody);
    }

    private static RequestHeader createRequestHeader(BufferedReader br) throws IOException {
        Map<String, String> map = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.equals(EMPTY)) {
                break;
            }
            String key = line.split(DOUBLE_DOT)[0];
            String value = line.split(DOUBLE_DOT)[1].trim();
            map.put(key.substring(0, key.length()-1), value);
        }
        return new RequestHeader(map);
    }

    /**
     * @param queryString은
     *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키
     *            값은 name1=value1; name2=value2 형식임
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
