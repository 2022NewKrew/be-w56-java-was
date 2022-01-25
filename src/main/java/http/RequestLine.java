package http;

import java.util.Objects;

public class RequestLine {
    public static final String DELIMITER = " ";
    public static final int PARAMETER_COUNT = 3;

    private final HttpMethod method;
    private final RequestTarget requestTarget;
    private final HttpVersion version;

    public RequestLine(HttpMethod method, RequestTarget requestTarget, HttpVersion version) {
        validateNull(method, requestTarget, version);
        this.method = method;
        this.requestTarget = requestTarget;
        this.version = version;
    }

    private void validateNull(HttpMethod method, RequestTarget requestTarget, HttpVersion version) {
        if(method == null || requestTarget == null || version == null) {
            throw new IllegalArgumentException();
        }
    }

    public static RequestLine create(String[] tokens) {
        HttpMethod method = HttpMethod.matchValue(tokens[0]);
        RequestTarget requestTarget = new RequestTarget(tokens[1]);
        HttpVersion version = HttpVersion.matchValue(tokens[2]);
        return new RequestLine(method, requestTarget, version);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object){
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

    public boolean equalMethod(HttpMethod method) {
        return this.method.equals(method);
    }

    public RequestTarget getPath() {
        return requestTarget;
    }
}
