package http;

public class RequestMessage {
    private final RequestLine requestLine;
    private final Headers headers;
    private final RequestBody requestBody;

    public RequestMessage(RequestLine requestLine, Headers headers, RequestBody requestBody) {
        validateNull(requestLine, headers);
        this.requestLine = requestLine;
        this.headers = headers;
        this.requestBody = requestBody;
    }

    public RequestMessage(RequestLine requestLine, Headers headers) {
        this(requestLine, headers, new RequestBody());
    }

    private void validateNull(RequestLine requestLine, Headers headers) {
        if (requestLine == null || headers == null) {
            throw new IllegalArgumentException();
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
