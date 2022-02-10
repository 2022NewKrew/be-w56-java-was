package webserver.http.message;

public class HttpHeader {

    private final String accept;
    private final String cookie;
    private final int contentLength;
    private final String contentType;

    //TODO:프로퍼티 많아지면 빌더패턴으로 변경
    public HttpHeader(String accept, String cookie, int contentLength, String contentType) {
        this.accept = accept;
        this.cookie = cookie;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    public String getAccept() {
        return accept;
    }

    public String getCookie() {
        return cookie;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }
}
