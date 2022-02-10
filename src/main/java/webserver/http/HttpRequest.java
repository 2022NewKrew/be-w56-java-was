package webserver.http;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import webserver.handler.ParsedParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String uri;
    private final String path;
    private final String httpVersion;
    private final Map<String, String> queryStrings;
    @Setter
    private ParsedParams parsedParams;

    @Builder
    public HttpRequest(HttpMethod httpMethod, String uri, String path, String httpVersion,
                       Map<String, String> queryStrings) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.path = path;
        this.httpVersion = httpVersion;
        this.queryStrings = queryStrings;
    }

    public static HttpRequest parseFrom(BufferedReader br) throws IOException {
        HttpRequest.HttpRequestBuilder httpRequestBuilder = HttpRequest.builder();
        parseRequestLine(br, httpRequestBuilder);
        parseRequestHeaders(br, httpRequestBuilder);
        return httpRequestBuilder.build();
    }

    private static void parseRequestLine(BufferedReader br,
                                         HttpRequest.HttpRequestBuilder httpRequestBuilder) throws IOException {
        String requestLine = br.readLine();

        String[] splitRequestLine = requestLine.split(" ");
        httpRequestBuilder.httpMethod(HttpMethod.getHttpMethod(splitRequestLine[0]))
                          .uri(splitRequestLine[1])
                          .path(splitRequestLine[1].substring(0, splitRequestLine[1].contains(
                                  "?") ? splitRequestLine[1].lastIndexOf("?") : splitRequestLine[1].length()))
                          .queryStrings(parseQueryString(
                                  splitRequestLine[1].substring(splitRequestLine[1].lastIndexOf("?") + 1)))
                          .httpVersion(splitRequestLine[2]);
    }

    private static void parseRequestHeaders(BufferedReader br,
                                            HttpRequest.HttpRequestBuilder httpRequestBuilder) throws IOException {
        // No support for any headers yet
    }

    /**
     * @param queryString URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies 쿠키값은 name1=value1; name2=value2 형식임
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
