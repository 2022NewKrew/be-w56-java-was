package webserver.http;

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

public class MyHttpRequest extends HttpRequest {

    private static final BiPredicate<String, String> ALLOWED_ALL_HEADERS = (k, v) -> true;
    private static final String END_OF_REQUEST_LINE = "";
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";
    private static final String HEADER_VALUE_DELIMITER = ",";

    private final String method;
    private final String requestURI;
    private final String version;
    private final Map<String, List<String>> headers;

    private MyHttpRequest(BufferedReader in) throws IOException {
        String[] requestHeaderParams = in.readLine().split(REQUEST_LINE_DELIMITER);
        this.method = requestHeaderParams[0];
        this.requestURI = requestHeaderParams[1];
        this.version = requestHeaderParams[2];
        this.headers = new HashMap<>();
        initHeaders(in);
    }

    private void initHeaders(BufferedReader in) throws IOException {
        String inputLine;
        while (!(inputLine = in.readLine()).equals(END_OF_REQUEST_LINE)) {
            String[] inputs = inputLine.split(HEADER_KEY_VALUE_DELIMITER);

            List<String> values = Arrays.stream(inputs[1].split(HEADER_VALUE_DELIMITER))
                    .map(String::trim)
                    .collect(Collectors.toList());

            headers.put(inputs[0], values);
        }
    }

    public static MyHttpRequest of(BufferedReader in) throws IOException {
        return new MyHttpRequest(in);
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        // TODO - Request Body 구현
        return Optional.empty();
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public Optional<Duration> timeout() {
        // 구현 X
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean expectContinue() {
        // 구현 X
        throw new UnsupportedOperationException();
    }

    @Override
    public URI uri() {
        return URI.create(requestURI);
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
