package http;

public class RequestMessage {
    private final RequestLine requestLine;
    private final Headers headers;

    public RequestMessage(RequestLine requestLine, Headers headers) {
        validateNull(requestLine, headers);
        this.requestLine = requestLine;
        this.headers = headers;
    }

    public void validateNull(RequestLine requestLine, Headers headers) {
        if(requestLine == null || headers == null) {
            throw new IllegalArgumentException();
        }
    }
}
