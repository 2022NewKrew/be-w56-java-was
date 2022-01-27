package webserver.request;

import java.util.List;

public class RequestLine {
    private final static String REGEX = " ";

    private final HttpRequestMethod httpRequestMethod;
    private final RequestUri requestUri;
    private final String httpVersion;

    private RequestLine(HttpRequestMethod httpRequestMethod, RequestUri requestUri, String httpVersion) {
        this.httpRequestMethod = httpRequestMethod;
        this.requestUri = requestUri;
        this.httpVersion = httpVersion;
    }

    public static RequestLine from(String line) {
        List<String> tokens = List.of(line.split(REGEX));
        validateInput(tokens);
        return new RequestLine(HttpRequestMethod.valueOf(tokens.get(0)), RequestUri.from(tokens.get(1)), tokens.get(2));
    }

    private static void validateInput(List<String> tokens) {
        if(tokens.size() != 3) throw new IllegalArgumentException("올바른 입력이 아닙니다.");
    }

    public HttpRequestMethod getHttpRequestMethod() {
        return httpRequestMethod;
    }

    public RequestUri getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "httpRequestMethod=" + httpRequestMethod +
                ", uri='" + requestUri + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
