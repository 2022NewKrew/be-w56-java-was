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
        System.out.println(httpVersion.getVersion());
        return httpVersion.getVersion() + SEPARATOR + statusCode + SEPARATOR + statusMessage;
    }
}
