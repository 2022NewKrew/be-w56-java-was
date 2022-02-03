package webserver.http;

import util.HttpRequestUtils;
import util.IOUtils;
import webserver.exception.BadRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class MyHttpRequest {

    private static final BiPredicate<String, String> ALLOWED_ALL_HEADERS = (k, v) -> true;
    private static final String END_OF_REQUEST_LINE = "";
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";
    private static final String HEADER_VALUE_DELIMITER = ",";

    private final HttpMethod method;
    private final String requestURI;
    private final String version;
    private final Map<String, List<String>> headers;
    private final Map<String, HttpCookie> cookies;
    private final String body;

    private MyHttpRequest(BufferedReader br) throws IOException {
        String[] requestHeaderParams = br.readLine().split(REQUEST_LINE_DELIMITER);
        validateRequestHeader(requestHeaderParams);
        this.method = HttpMethod.valueOf(requestHeaderParams[0]);
        this.requestURI = requestHeaderParams[1];
        this.version = requestHeaderParams[2];
        this.headers = readRequestHeaderFromBuffer(br);
        this.cookies = getCookieFromHeaders();
        this.body = readRequestBodyFromBuffer(br, contentLength());
    }

    private String readRequestBodyFromBuffer(BufferedReader br, int contentLength) throws IOException {
        String body = IOUtils.readData(br, contentLength);
        return URLDecoder.decode(body, StandardCharsets.UTF_8);
    }

    private void validateRequestHeader(String[] requestHeaderParams) {
        if (requestHeaderParams.length != 3) {
            throw new BadRequestException("에러: 요청 헤더가 적절하지 않습니다.");
        }
    }

    private Map<String, List<String>> readRequestHeaderFromBuffer(BufferedReader br) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        String inputLine;
        while (!(inputLine = br.readLine()).equals(END_OF_REQUEST_LINE)) {
            String[] inputs = inputLine.split(HEADER_KEY_VALUE_DELIMITER);

            List<String> values = Arrays.stream(inputs[1].split(HEADER_VALUE_DELIMITER))
                    .map(String::trim)
                    .collect(Collectors.toList());

            headers.put(inputs[0], values);
        }
        return headers;
    }

    private Map<String, HttpCookie> getCookieFromHeaders() {
        if (headers.containsKey("Cookie")) {
            Map<String, HttpCookie> cookies = new HashMap<>();
            List<String> cookie = headers.get("Cookie");
            String cookieString = cookie.get(0);
            Map<String, String> cookiesMap = HttpRequestUtils.parseCookies(cookieString);
            cookiesMap.forEach((k, v) -> cookies.put(k, new HttpCookie(k, v)));
            return cookies;
        }
        return Collections.emptyMap();
    }

    private int contentLength() {
        if (headers.containsKey("Content-Length")) {
            List<String> values = headers.get("Content-Length");
            return Integer.parseInt(values.get(0));
        }
        return 0;
    }

    public static MyHttpRequest from(BufferedReader br) throws IOException {
        return new MyHttpRequest(br);
    }

    public String method() {
        return method.name();
    }

    public URI uri() {
        return URI.create(requestURI);
    }

    public Optional<HttpClient.Version> version() {
        if (version.equals("HTTP/1.1")) {
            return Optional.of(HttpClient.Version.HTTP_1_1);
        }
        if (version.equals("HTTP/2.0")) {
            return Optional.of(HttpClient.Version.HTTP_2);
        }
        return Optional.empty();
    }

    public HttpHeaders headers() {
        return HttpHeaders.of(headers, ALLOWED_ALL_HEADERS);
    }

    public Map<String, HttpCookie> cookies() {
        return cookies;
    }

    public String body() {
        return body;
    }

    @Override
    public String toString() {
        return "MyHttpRequest{" +
                "method='" + method + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                '}';
    }
}
