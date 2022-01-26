package webserver.domain;

import lombok.Builder;

public class StatusLine {

    private static final String STATUS_LINE_FORMAT = "%s %s \r\n";
    private static final String HTTP_VERSION_1_1 = "HTTP/1.1";

    private final String httpVersion;
    private final HttpStatus httpStatus;

    @Builder
    private StatusLine(String httpVersion, HttpStatus httpStatus) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
    }

    public StatusLine(StatusLine statusLine) {
        this(statusLine.httpVersion, statusLine.httpStatus);
    }

    public static StatusLine createStatus(HttpStatus httpStatus) {
        return StatusLine.builder()
            .httpVersion(HTTP_VERSION_1_1)
            .httpStatus(httpStatus)
            .build();
    }

    @Override
    public String toString() {
        return String.format(STATUS_LINE_FORMAT, httpVersion, httpStatus);
    }
}
