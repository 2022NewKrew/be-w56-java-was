package webserver.response;

public class StatusLine {
    private final String httpVersion;
    private final HttpStatusCode httpStatusCode;

    public StatusLine(String httpVersion, HttpStatusCode httpStatusCode) {
        this.httpVersion = httpVersion;
        this.httpStatusCode = httpStatusCode;
    }
}
