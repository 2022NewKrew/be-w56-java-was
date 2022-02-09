package util;

import exception.InvalidRequestLineException;
import http.MediaType;
import http.HttpHeaders;
import http.request.URI;
import http.request.Queries;
import http.request.RequestBody;
import http.request.RequestLine;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import http.HttpMethod;

public class HttpRequestUtils {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String PATH_QUERY_STRING_DELIMITER = "\\?";
    private static final String PARAMETER_DELIMITER = "&";
    private static final String COOKIE_DELIMITER = ";";
    private static final String PARAMETER_KEY_VALUE_DELIMITER = "=";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";

    private HttpRequestUtils() {
    }

    public static RequestLine parseRequestLine(String requestLine) {
        if (Strings.isNullOrEmpty(requestLine)) {
            throw new InvalidRequestLineException("null");
        }

        String[] tokens = requestLine.split(REQUEST_LINE_DELIMITER);

        if (tokens.length != 3) {
            throw new InvalidRequestLineException(requestLine);
        }

        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        String path = parsePath(tokens[1]);
        Queries queries = parseQueries(tokens[1]);

        return new RequestLine(method, new URI(path, queries));
    }

    public static String parsePath(String targetToken) {
        return targetToken.split(PATH_QUERY_STRING_DELIMITER)[0];
    }

    public static Queries parseQueries(String targetToken) {
        String[] tokens = targetToken.split(PATH_QUERY_STRING_DELIMITER);
        if (tokens.length < 2) {
            return Queries.empty();
        }

        return new Queries(parseValues(tokens[1], PARAMETER_DELIMITER));
    }

    public static RequestBody parseRequestBody(MediaType contentType, String body) {
        if (Strings.isNullOrEmpty(body)) {
            return RequestBody.empty();
        }

        return new RequestBody(parseValues(body, PARAMETER_DELIMITER));
    }

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

        return new Pair(URLDecoder.decode(tokens[0], StandardCharsets.UTF_8), URLDecoder.decode(tokens[1], StandardCharsets.UTF_8));
    }

    public static HttpHeaders parseHeaders(List<String> headerStrings) {
        Map<String, String> headers = headerStrings.stream()
                .map(HttpRequestUtils::parseHeader)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        return new HttpHeaders(headers);
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
