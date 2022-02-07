package infrastructure.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import infrastructure.model.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    private static final String REQUEST_SEPARATE_TOKEN = " ";

    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static Map<String, String> parseBody(String body) {
        return parseValues(body, "&");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
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

    public static RequestLine parseRequestLine(String requestLine) throws NullPointerException, IllegalArgumentException {
        String[] line = requestLine.split(REQUEST_SEPARATE_TOKEN);
        RequestMethod requestMethod = RequestMethod.getMethod(line[0])
                .orElseThrow(IllegalArgumentException::new);
        Path path = parsePath(line[1]);

        return new RequestLine(requestMethod, path);
    }

    private static Path parsePath(String path) {
        if (!path.contains("?")) {
            return Path.builder()
                    .value(path)
                    .contentType(ContentType.valueOfPath(path))
                    .build();
        }

        String[] split = path.split("\\?");
        path = split[0];
        String queryString = split[1];
        return Path.builder()
                .value(path)
                .contentType(ContentType.valueOfPath(path))
                .variables(parseQueryString(queryString))
                .build();
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }
}
