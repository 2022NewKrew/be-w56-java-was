package http.response;

import http.HttpStatus;

public class StatusLine {

    private String protocolVersion;

    private String statusCode;

    private String statusText;

    private StatusLine(String protocolVersion, String statusCode, String statusText) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public static StatusLine of(String protocolVersion, HttpStatus httpStatus) {
        return new StatusLine(protocolVersion, httpStatus.getCode(), httpStatus.getText());
    }
}
