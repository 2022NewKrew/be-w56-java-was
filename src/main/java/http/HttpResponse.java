package http;

import collections.ResponseHeaders;

import java.util.Map;

public class HttpResponse {

    private String statusLine;
    private ResponseHeaders headers;
    private byte[] body;

    public HttpResponse(String statusLine, ResponseHeaders headers, byte[] body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse create200ForwordHttpResponse(Map<String, String> headers, byte[] body) {
        return new HttpResponse("HTTP/1.1 200 OK", new ResponseHeaders(headers), body);
    }

    public static HttpResponse create302RedirectHttpResponse(Map<String, String> headers) {
        return new HttpResponse("HTTP/1.1 302 Found", new ResponseHeaders(headers), null);
    }

    public ResponseHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String getStatusLine() {
        return statusLine;
    }
}
