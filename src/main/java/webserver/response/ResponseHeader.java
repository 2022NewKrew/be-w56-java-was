package webserver.response;

public class ResponseHeader {
    private final String contentType;
    private final int contentLength;

    private ResponseHeader(String contentType, int contentLength) {
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public static ResponseHeader of(String contentType, int contentLength) {
        return new ResponseHeader(contentType, contentLength);
    }
}
