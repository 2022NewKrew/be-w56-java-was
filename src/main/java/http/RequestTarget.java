package http;

import util.ParsingUtils;

import java.util.Objects;

public class RequestTarget {
    public static final String DELIMITER = "\\?";
    public static final int PARAMETER_COUNT = 2;

    private final Path path;
    private final Parameters parameters;

    public RequestTarget(Path path) {
        this(path, new Parameters());
    }

    public RequestTarget(Path path, Parameters parameters) {
        validateNull(path);
        this.path = path;
        this.parameters = parameters;
    }

    public static RequestTarget create(String requestTarget) {
        String[] token = ParsingUtils.parse(requestTarget, DELIMITER);
        Path path = new Path(token[0]);
        Parameters parameters = new Parameters();
        if (token.length == PARAMETER_COUNT) {
            parameters = Parameters.create(token[1]);
        }
        return new RequestTarget(path, parameters);
    }

    private void validateNull(Path path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RequestTarget requestTarget = (RequestTarget) object;
        return Objects.equals(this.path, requestTarget.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    public Path getPath() {
        return path;
    }

    public Parameters getQueryParameters() {
        return parameters;
    }
}
