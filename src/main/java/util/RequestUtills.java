package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static util.punctuationMarksUtils.*;

public class RequestUtills {

    private static final Logger log = LoggerFactory.getLogger(RequestUtills.class);

    private RequestUtills() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] readRequestMainHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.info("REQ --- : {}", line);
        return line.split(SPACE);
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> map = new HashMap<>();
        String line;
        int contentLength = 0;
        while ((line = br.readLine()) != null) {
            if (line.equals(EMPTY)) {
                break;
            }
            String key = line.split(DOUBLE_DOT)[0];
            String value = line.split(DOUBLE_DOT)[1].trim();
            map.put(key, value);
            if (key.equals("Content-Length")) {
                contentLength = Integer.valueOf(value);
            }
        }

        String queryString = IOUtils.readData(br, contentLength);
        map.put("queryString", queryString);

        return map;
    }

    public static Request generateRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] requestLine = readRequestMainHeader(br);
        Map<String, String> header = readHeader(br);
        String queryString = readQueryString(requestLine, header);
        Map<String, String> queries = parseValues(queryString, AMPERSAND);
        Map<String, String> cookies = parseCookies(header.get("Cookie"));
        return Request.of(requestLine, header, queries, cookies);
    }

    private static String readQueryString(String[] requestLine, Map<String, String> headers) {
        String httpMethod = requestLine[0];
        if (httpMethod.equals("GET")) {
            String[] uri = requestLine[1].split(QUESTION_MARK);
            return uri.length > 1 ? uri[1] : "";
        }
        return headers.get("queryString");
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, "; ");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static RequestUtills.Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new RequestUtills.Pair(tokens[0], tokens[1]);
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
            RequestUtills.Pair other = (RequestUtills.Pair) obj;
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
