package model;

public class RequestHeader {

    private int contentLength;
    private String contentType;
    private boolean cookie;

    public RequestHeader(int contentLength, String contentType, boolean cookie) {
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.cookie = cookie;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isCookie() {
        return cookie;
    }
}
