package http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private RequestLine requestLine;
    private HttpHeader header;
    private RequestBody requestBody;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        this.requestLine = new RequestLine(bufferedReader);
        this.header = new HttpHeader(bufferedReader);
        if (header.getHeaders().get("Content-Length") != null) {
            this.requestBody = new RequestBody(bufferedReader, Integer.parseInt(header.getHeaders().get("Content-Length")));
        }
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getQueryString(String key) {
        return requestLine.getQueryString(key);
    }

    public String getHeader(String key) {
        return header.getValue(key);
    }

    public String getRequestBody(String key) {
        return requestBody.getValue(key);
    }
}
