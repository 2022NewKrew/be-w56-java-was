package http.request;

import java.io.BufferedReader;

public class HttpRequest {

    private final BufferedReader bufferedReader;
    private final HttpRequestStartLine httpRequestStartLine;
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    private HttpRequest(BufferedReader bufferedReader, HttpRequestStartLine httpRequestStartLine,
        HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.bufferedReader = bufferedReader;
        this.httpRequestStartLine = httpRequestStartLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public static HttpRequest of(BufferedReader br) {
        HttpRequestStartLine httpRequestStartLine = HttpRequestStartLine.of(br);
        HttpRequestHeader httpRequestHeader = HttpRequestHeader.of(br);
        Integer contentLength = httpRequestHeader.getContentLength();
        HttpRequestBody httpRequestBody = HttpRequestBody.of(br, contentLength);

        return new HttpRequest(br, httpRequestStartLine, httpRequestHeader, httpRequestBody);
    }

    public boolean hasValue() {
        return httpRequestStartLine.hasValue();
    }

    public boolean validate() { return hasValue(); }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public HttpRequestStartLine getRequestStartLine() {
        return httpRequestStartLine;
    }

    public HttpRequestHeader getRequestHeader() {
        return httpRequestHeader;
    }

    public HttpRequestBody getRequestBody() {
        return httpRequestBody;
    }

    public String getMethod() {
        return httpRequestStartLine.getMethod();
    }

    public String getUrl() {
        return httpRequestStartLine.getUrl();
    }

    public String getProtocol() {
        return httpRequestStartLine.getProtocol();
    }
}
