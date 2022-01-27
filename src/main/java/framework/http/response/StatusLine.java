package framework.http.response;

import framework.http.enums.HttpStatus;

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
}
