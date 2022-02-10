package model;

public class StatusLine {

    private static String SEPARATOR = " ";

    private final HttpVersion httpVersion;
    private final int statusCode;
    private final String statusMessage;

    public StatusLine(HttpVersion httpVersion, int statusCode, String statusMessage) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public String message() {
        return httpVersion.name() + SEPARATOR + statusCode + SEPARATOR + statusMessage;
    }
}
