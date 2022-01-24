package model;

public class Request {
    private final RequestLine requestLine;
    private final RequestHeaders requestHeaders;

    private Request(RequestLine requestLine, RequestHeaders requestHeaders) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
    }

    public static Request of(RequestLine requestLine, RequestHeaders requestHeaders) {
        return new Request(requestLine, requestHeaders);
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }
}
