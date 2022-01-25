package webserver.http.response;

import webserver.http.common.Mime;

public class HttpResponseBody {
    private final byte[] content;
    private final Mime mime;

    private HttpResponseBody(byte[] content, Mime mime) {
        this.content = content;
        this.mime = mime;
    }

    public static HttpResponseBody of(byte[] viewByteArray, String viewName) {
        return new HttpResponseBody(viewByteArray, Mime.of(viewName));
    }

    public byte[] getContent() {
        return content;
    }

    public int getLength() {
        return content.length;
    }

    public String getContentType() {
        return mime.getContentType();
    }

    @Override
    public String toString() {
        return "HttpResponseBody{" +
                "mime=" + mime +
                '}';
    }
}
