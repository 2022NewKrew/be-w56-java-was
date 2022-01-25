package http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private HttpHeader header;
    private RequestLine requestLine;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        this.requestLine = new RequestLine(bufferedReader);
        this.header = new HttpHeader(bufferedReader);
    }

    public HttpHeader getHeader() {
        return header;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }
}
