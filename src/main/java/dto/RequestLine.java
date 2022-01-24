package dto;

public class RequestLine {
    private final String method;
    private final String path;
    private final String version;

    public RequestLine(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public RequestLine(String[] tokens) {
        this.method = tokens[0];
        this.path = tokens[1];
        this.version = tokens[2];
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }
}
