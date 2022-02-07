package model;

public class StatusLine {

    private static String SEPARATOR = " ";

    private final String httpVersion;
    private final int statusCode;
    private final String statusMessage;

    public StatusLine(String httpVersion, int statusCode, String statusMessage) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public String message() {
        return httpVersion + SEPARATOR + statusCode + SEPARATOR + statusMessage;
    }
}
