package web.http.response;

public class HttpResponseStatusLine {
    private final String version;
    private final HttpStatus status;

    public HttpResponseStatusLine(String version, HttpStatus status) {
        this.version = version;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return version + " " + status.getCode() + " " + status.getText();
    }
}
