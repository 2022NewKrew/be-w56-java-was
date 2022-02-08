package http.startline;

import http.Parameters;
import http.Path;
import util.ParsingUtils;

public class RequestTarget {
    public static final String DELIMITER = "\\?";
    public static final int PARAMETER_COUNT = 2;

    private final Path path;
    private final Parameters parameters;

    private RequestTarget(Path path, Parameters parameters) {
        validateNull(path);
        this.path = path;
        this.parameters = parameters;
    }

    public static RequestTarget create(String requestTarget) {
        String[] token = ParsingUtils.parse(requestTarget, DELIMITER);
        if (token.length == PARAMETER_COUNT) {
            return new RequestTarget(new Path(token[0]), Parameters.create(token[1]));
        }
        return new RequestTarget(new Path(token[0]), Parameters.create(null));
    }

    private void validateNull(Path path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
    }

    public Path getPath() {
        return path;
    }

    public Parameters getQueryParameters() {
        return parameters;
    }

    public String createStaticPath() {
        return path.createStaticPath();
    }
}
