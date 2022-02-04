package request;

import java.io.BufferedReader;

public class HttpRequest {

    private final BufferedReader bufferedReader;
    private final RequestStartLine requestStartLine;
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;

    private HttpRequest(BufferedReader bufferedReader, RequestStartLine requestStartLine,
        RequestHeader requestHeader, RequestBody requestBody) {
        this.bufferedReader = bufferedReader;
        this.requestStartLine = requestStartLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static HttpRequest of(BufferedReader br) {
        RequestStartLine requestStartLine = RequestStartLine.of(br);
        RequestHeader requestHeader = RequestHeader.of(br);
        Integer contentLength = requestHeader.getContentLength();
        RequestBody requestBody = RequestBody.of(br, contentLength);

        return new HttpRequest(br, requestStartLine, requestHeader, requestBody);
    }

    public boolean hasValue() {
        return requestStartLine.hasValue();
    }

    public boolean validate() { return hasValue(); }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public RequestStartLine getRequestStartLine() {
        return requestStartLine;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public String getMethod() {
        return requestStartLine.getMethod();
    }

    public String getUrl() {
        return requestStartLine.getUrl();
    }

    public String getProtocol() {
        return requestStartLine.getProtocol();
    }
}
