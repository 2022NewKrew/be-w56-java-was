package http;

import util.ParsingUtils;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RequestLine that = (RequestLine) object;
        return method == that.method && Objects.equals(requestTarget, that.requestTarget) && version == that.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, requestTarget, version);
    }

    public RequestTarget getRequestTarget() {
        return requestTarget;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
