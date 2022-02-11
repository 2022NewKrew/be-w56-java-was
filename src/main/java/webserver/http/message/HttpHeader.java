package webserver.http.message;

import java.util.Map;

public class HttpHeader {

    private final String accept;
    private final Map<String, String> cookie;
    private final int contentLength;
    private final String contentType;

    public HttpHeader(String accept, Map<String, String> cookie, int contentLength, String contentType) {
        this.accept = accept;
        this.cookie = cookie;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    public String getAccept() {
        return accept;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }
}
