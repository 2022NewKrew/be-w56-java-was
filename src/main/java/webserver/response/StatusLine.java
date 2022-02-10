package webserver.response;

public class StatusLine {
    private final String httpVersion;
    private final HttpStatusCode httpStatusCode;

    private StatusLine(String httpVersion, HttpStatusCode httpStatusCode) {
        this.httpVersion = httpVersion;
        this.httpStatusCode = httpStatusCode;
    }

    public static StatusLine of(String httpVersion, HttpStatusCode httpStatusCode) {
        return new StatusLine(httpVersion, httpStatusCode);
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String toString() {
        return "StatusLine{" +
                "httpVersion='" + httpVersion + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }
}
