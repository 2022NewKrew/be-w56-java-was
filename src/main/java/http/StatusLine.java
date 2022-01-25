package http;

public class StatusLine {
    private String protocol;
    private HttpStatus httpStatus;

    public StatusLine(String protocol, HttpStatus httpStatus) {
        this.protocol = protocol;
        this.httpStatus = httpStatus;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getStatusCode() {
        return httpStatus.getStatusCode();
    }

    public String getStatusText() {
        return httpStatus.getStatusText();
    }
}
