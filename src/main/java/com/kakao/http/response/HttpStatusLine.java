package com.kakao.http.response;

public class HttpStatusLine {
    private final String version;
    private final HttpStatus status;

    public HttpStatusLine(String version, HttpStatus status) {
        this.version = version;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s\r\n", version, status.getCode(), status.getText());
    }
}
