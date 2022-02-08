package webserver.http.request;

public class HttpRequest {

    private final HttpHeader header;
    private final HttpBody body;
    private final HttpUrlQuery query;

    public HttpRequest(HttpHeader header, HttpBody body, HttpUrlQuery query) {
        this.header = header;
        this.body = body;
        this.query = query;
    }

    public String getQueryAttribute(String key) {
        return query.getQueryAttribute(key);
    }

    public String getBodyAttribute(String key) {
        return body.getBodyAttribute(key);
    }

    public int bodyValueCount() {
        return body.valueCount();
    }

    public String getHeaderAttribute(String key) {
        return header.getHeaderAttribute(key);
    }

    public String getCookie(String key) {
        return header.getCookie(key);
    }
}
