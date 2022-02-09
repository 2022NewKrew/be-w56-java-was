package http.request;

import http.HttpHeaders;
import java.util.Map;
import util.HttpRequestUtils;

public class HttpRequest {

    private static final String COOKIE = "Cookie";
    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private final RequestBody body;

    public HttpRequest(RequestLine requestLine, HttpHeaders headers, RequestBody body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestBody getBody() {
        return body;
    }

    public Map<String, String> getCookies() {
        String cookies = headers.get(COOKIE);
        return HttpRequestUtils.parseCookies(cookies);
    }
}
