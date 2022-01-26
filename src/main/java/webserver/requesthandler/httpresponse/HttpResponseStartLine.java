package webserver.requesthandler.httpresponse;

public class HttpResponseStartLine {
    private final String version;
    private final HttpStatus httpStatus;

    public HttpResponseStartLine(String httpVersion, HttpStatus httpStatus) {
        this.version = httpVersion;
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return this.version + " " + this.httpStatus.toString() + "\r\n";
    }
}
