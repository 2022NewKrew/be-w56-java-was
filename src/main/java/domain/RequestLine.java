package domain;

public class RequestLine {

    private static final int REQUEST_LINE_SIZE = 3;

    private final HttpMethod method;
    private final String path;
    private final String version;

    public RequestLine(String requestLine) {
        validateRequestLine(requestLine);
        String[] tokens = splitRequestLine(requestLine);

        this.method = HttpMethod.valueOf(tokens[0]);
        this.path = tokens[1];
        this.version = tokens[2];
    }

    private void validateRequestLine(String requestLine) {
        if (splitRequestLine(requestLine).length != REQUEST_LINE_SIZE) {
            throw new IllegalArgumentException("잘못된 RequestLine입니다.");
        }
    }

    private String[] splitRequestLine(String requestLine) {
        return requestLine.split(Constants.SPACE_DELIMITER);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }
}
