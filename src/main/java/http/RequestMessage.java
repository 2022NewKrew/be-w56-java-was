package http;

import java.io.File;

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

    public Headers getHeader() {
        return headers;
    }

    public File createStaticFile() {
        return requestLine.createStaticFile();
    }
}
