package model;

public class StatusLine {

    private static String SEPARATOR = " ";

    private final String httpVersion;
    private final int statusCode;
    private final String statusText;

    public StatusLine(String httpVersion, int statusCode, String statusText) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public String message() {
        return httpVersion + SEPARATOR + statusCode + SEPARATOR + statusText;
    }
}
