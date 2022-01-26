package model;

public class RequestHeader {

    private int contentLength;
    private String contentType;

    public RequestHeader(int contentLength, String contentType) {
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }
}
