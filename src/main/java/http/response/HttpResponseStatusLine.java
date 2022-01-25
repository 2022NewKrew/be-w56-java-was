package http.response;

public class HttpResponseStatusLine {
    private final String version;
    private final String statusCode;
    private final String statusText;

    public HttpResponseStatusLine(String version, String statusCode, String statusText) {
        this.version = version;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public String getProtocol() {
        return version;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    @Override
    public String toString() {
        return version + " " + statusCode + " " + statusText;
    }
}
