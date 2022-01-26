package http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private HttpHeader header;
    private RequestLine requestLine;
    private RequestBody requestBody;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        this.requestLine = new RequestLine(bufferedReader);
        this.header = new HttpHeader(bufferedReader);
        if (header.getHeaders().get("Content-Length") != null) {
            this.requestBody = new RequestBody(bufferedReader, Integer.parseInt(header.getHeaders().get("Content-Length")));
        }
    }

    public HttpHeader getHeader() {
        return header;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
