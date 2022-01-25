package infrastructure.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import infrastructure.model.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    private static final String REQUEST_SEPARATE_TOKEN = " ";

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

    public static RequestLine parseRequestLine(String requestLine) throws IllegalArgumentException {
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
