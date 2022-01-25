package webserver.http.dto;

import webserver.http.Packet.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class CustomHttpRequest extends HttpRequest {

    private static final BiPredicate<String, String> ALLOWED_ALL_HEADERS = (k, v) -> true;
    private static final String END_OF_REQUEST_LINE = "";
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";
    private static final String HEADER_VALUE_DELIMITER = ",";

    private final HttpMethod method;
    private final String uri;
    private final String version;
    private final Map<String, List<String>> headers;

    private CustomHttpRequest(BufferedReader br) throws IOException {
        String[] requestParams = br.readLine().split(REQUEST_LINE_DELIMITER);
        method = HttpMethod.parse(requestParams[0]);
        uri = requestParams[1];
        version = requestParams[2];
        headers = new HashMap<>();
        initHeaders(br);
    }

    private void initHeaders(BufferedReader br) throws IOException {
        String inputLine;
        while(!(inputLine = br.readLine()).equals(END_OF_REQUEST_LINE)) {
            String[] inputs = inputLine.split(HEADER_KEY_VALUE_DELIMITER);
            List<String> values = Arrays.stream(inputs[1].split(HEADER_VALUE_DELIMITER))
                    .map(String::trim)
                    .collect(Collectors.toList());
            headers.put(inputs[0], values);
        }
    }

    public static CustomHttpRequest of(BufferedReader br) throws IOException {
        return new CustomHttpRequest(br);
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        return Optional.empty();
    }

    @Override
    public String method() {
        return method.getMethod();
    }

    public HttpMethod getHttpMethod() {
        return method;
    }

    @Override
    public Optional<Duration> timeout() {
        return Optional.empty();
    }

    @Override
    public boolean expectContinue() {
        return false;
    }

    @Override
    public URI uri() {
        return URI.create(uri);
    }

    @Override
    public Optional<HttpClient.Version> version() {
        if (version.equals("HTTP/1.1")) {
            return Optional.of(HttpClient.Version.HTTP_1_1);
        }
        if (version.equals("HTTP/2.0")) {
            return Optional.of(HttpClient.Version.HTTP_2);
        }
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(headers, ALLOWED_ALL_HEADERS);
    }
}
