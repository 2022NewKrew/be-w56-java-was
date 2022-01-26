package util.http;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(Objects::nonNull)
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

        return new Pair(tokens[0], URLDecoder.decode(tokens[1], StandardCharsets.UTF_8));
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static class Pair {
        String key;
        String value;

        public Pair(String key, String value) {
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

    public static HttpRequest parseRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] words = line.split(" ");

        HttpMethod httpMethod = HttpMethod.valueOf(words[0]);
        URL url = new URL(words[1]);
        String httpVersion = words[2];

        List<Pair> pairs = new ArrayList<>();
        while (!line.equals("")) {
            line = br.readLine();
            pairs.add(parseHeader(line));
            log.debug("header : {}", line);
        }

        Map<String, String> headers = pairs.stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        Map<String, String> body = null;

        //body parsing if post
        if (httpMethod == HttpMethod.POST) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            String queryString = IOUtils.readData(br, contentLength);
            body = parseQueryString(queryString);
            return new HttpRequest(httpMethod, url, httpVersion, headers, body);
        }
        return new HttpRequest(httpMethod, url, httpVersion, headers);
    }


}
