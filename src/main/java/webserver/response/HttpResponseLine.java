package webserver.response;

public class HttpResponseLine {

    private static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    private String httpVersion;
    private HttpStatus status;

    public HttpResponseLine(HttpStatus status) {
        this.httpVersion = DEFAULT_HTTP_VERSION;
        this.status = status;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getStatusCode() {
        return status.getNum() + " " + status;
    }
}
