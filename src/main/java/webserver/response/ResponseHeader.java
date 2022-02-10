package webserver.response;

public class ResponseHeader {
    private final String contentType;
    private final int contentLength;
    private final Boolean setCookie;

    public ResponseHeader(String contentType, int contentLength, Boolean setCookie) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.setCookie = setCookie;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public Boolean getSetCookie() {
        return setCookie;
    }

    public static ResponseHeader of(String contentType, int contentLength) {
        return new ResponseHeader(contentType, contentLength, false);
    }

    public static ResponseHeader of(String contentType, int contentLength, Boolean setCookie) {
        return new ResponseHeader(contentType, contentLength, setCookie);
    }

    @Override
    public String toString() {
        return "ResponseHeader{" +
                "contentType='" + contentType + '\'' +
                ", contentLength=" + contentLength +
                ", setCookie=" + setCookie +
                '}';
    }
}
