package domain;

public class RequestLine {

    private static final int REQUEST_LINE_SIZE = 3;
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;

    private final HttpMethod method;
    private final String path;
    private final String version;

    public RequestLine(String requestLine) {
        validateRequestLine(requestLine);
        String[] tokens = splitRequestLine(requestLine);

        this.method = HttpMethod.valueOf(tokens[METHOD_INDEX]);
        this.path = tokens[PATH_INDEX];
        this.version = tokens[VERSION_INDEX];
    }

    private void validateRequestLine(String requestLine) {
        if (splitRequestLine(requestLine).length != REQUEST_LINE_SIZE) {
            throw new IllegalArgumentException("잘못된 RequestLine입니다.");
        }
    }

    private String[] splitRequestLine(String requestLine) {
        return requestLine.split(Constants.SPACE_DELIMITER);
    }

    public boolean isGetMethod() {
        return method == HttpMethod.GET;
    }
    public boolean isPostMethod() {
        return method == HttpMethod.POST;
    }

    public String getMethod() {
        return method.name();
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return method + " " + path + " " +  version;
    }
}
