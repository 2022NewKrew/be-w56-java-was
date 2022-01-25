package http;

public class HttpRequest {

    private RequestLine requestLine;
    private Header header;

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Header getHeader() {
        return header;
    }
}
