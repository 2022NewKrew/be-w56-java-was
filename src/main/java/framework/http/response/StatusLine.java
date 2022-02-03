package framework.http.response;

public class StatusLine {
    private static final String EMPTY_SPACE = " ";

    private String protocol;
    private HttpStatus httpStatus;

    public StatusLine(String protocol, HttpStatus httpStatus) {
        this.protocol = protocol;
        this.httpStatus = httpStatus;
    }

    public String getStatusLineText() {
        return protocol + EMPTY_SPACE + httpStatus.getStatusCode() + EMPTY_SPACE + httpStatus.getStatusText();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
