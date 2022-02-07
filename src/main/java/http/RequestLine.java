package http;

import util.ParsingUtils;

import java.io.File;

public class RequestLine {
    public static final String DELIMITER = " ";
    public static final int PARAMETER_COUNT = 3;

    private final HttpMethod method;
    private final RequestTarget requestTarget;
    private final HttpVersion version;

    private RequestLine(HttpMethod method, RequestTarget requestTarget, HttpVersion version) {
        validateNull(method, requestTarget, version);
        this.method = method;
        this.requestTarget = requestTarget;
        this.version = version;
    }

    private void validateNull(HttpMethod method, RequestTarget requestTarget, HttpVersion version) {
        if (method == null || requestTarget == null || version == null) {
            throw new IllegalArgumentException();
        }
    }

    public static RequestLine create(String line) {
        String[] tokens = ParsingUtils.parse(line, RequestLine.DELIMITER, RequestLine.PARAMETER_COUNT);
        HttpMethod method = HttpMethod.matchValue(tokens[0]);
        RequestTarget requestTarget = RequestTarget.create(tokens[1]);
        HttpVersion version = HttpVersion.matchValue(tokens[2]);
        return new RequestLine(method, requestTarget, version);
    }

    public RequestTarget getRequestTarget() {
        return requestTarget;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public File createStaticFile() {
        return new File(requestTarget.createStaticPath());
    }
}
