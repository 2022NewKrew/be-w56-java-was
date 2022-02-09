package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import webserver.exception.BadRequestException;
import webserver.http.*;

import static webserver.http.Header.CONTENT_LENGTH;
import static webserver.http.Header.COOKIE;

public class HttpRequestUtils {

    private static final String END_OF_REQUEST_LINE = "";
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";
    private static final String HEADER_VALUE_DELIMITER = ",";

    public static HttpRequest parseRequest(BufferedReader br) throws IOException {
        // REQUEST LINE
        String requestLine = br.readLine();
        String[] requestLineParams = requestLine.split(REQUEST_LINE_DELIMITER);
        validateRequestLine(requestLineParams);
        HttpMethod method = HttpMethod.valueOf(requestLineParams[0]);
        String requestURI = requestLineParams[1];
        String version = requestLineParams[2];

        // REQUEST HEADER
        HttpHeader headers = readRequestHeaderFromBuffer(br);
        HttpCookie cookies = getCookieFromHeaders(headers);

        // REQUEST BODY
        int contentLength = getContentLength(headers);
        String body = readRequestBodyFromBuffer(br, contentLength);

        return new HttpRequest(method, requestURI, version, headers, cookies, body);
    }

    private static void validateRequestLine(String[] requestLineParams) {
        if (requestLineParams.length != 3) {
            throw new BadRequestException("에러: 요청 헤더가 적절하지 않습니다.");
        }
    }

    private static HttpHeader readRequestHeaderFromBuffer(BufferedReader br) throws IOException {
        Map<Header, List<String>> headers = new HashMap<>();
        String inputLine;
        while (!(inputLine = br.readLine()).equals(END_OF_REQUEST_LINE)) {
            String[] inputs = inputLine.split(HEADER_KEY_VALUE_DELIMITER);

            Header key = Header.from(inputs[0]);
            List<String> values = Arrays.stream(inputs[1].split(HEADER_VALUE_DELIMITER))
                    .map(String::trim)
                    .collect(Collectors.toList());

            headers.put(key, values);
        }
        return new HttpHeader(headers);
    }

    private static HttpCookie getCookieFromHeaders(HttpHeader headers) {
        HttpCookie httpCookie = new HttpCookie();
        if (headers.containsKey(COOKIE)) {
            List<String> cookies = headers.getValues(COOKIE);
            String joinedCookies = String.join(HEADER_VALUE_DELIMITER, cookies);
            Map<String, String> cookiesMap = HttpRequestUtils.parseCookies(joinedCookies);
            cookiesMap.forEach((k, v) -> httpCookie.putCookie(new Cookie(k, v)));
        }
        return httpCookie;
    }

    private static int getContentLength(HttpHeader headers) {
        if (headers.containsKey(CONTENT_LENGTH)) {
            List<String> values = headers.getValues(CONTENT_LENGTH);
            return Integer.parseInt(values.get(0));
        }
        return 0;
    }

    private static String readRequestBodyFromBuffer(BufferedReader br, int contentLength) throws IOException {
        String body = IOUtils.readData(br, contentLength);
        return URLDecoder.decode(body, StandardCharsets.UTF_8);
    }

    /**
     * @param queryString
     * URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, List<String>> parseQueryString(String queryString) {
        if (Strings.isNullOrEmpty(queryString)) {
            return Maps.newHashMap();
        }
        Map<String, List<String>> resultMap = new HashMap<>();
        String[] tokens = queryString.split("&");

        Arrays.stream(tokens)
                .map(t -> getKeyValue(t, "="))
                .filter(Objects::nonNull)
                .forEach(p -> {
                    List<String> values = resultMap.getOrDefault(p.getKey(), new ArrayList<>());
                    values.add(p.getValue());
                    resultMap.put(p.getKey(), values);
                });
        return resultMap;
    }

    /**
     * @param cookies
     * 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        if (Strings.isNullOrEmpty(cookies)) {
            return Maps.newHashMap();
        }

        String[] tokens = cookies.split(";");
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "="))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (p1, p2) -> p2));
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
