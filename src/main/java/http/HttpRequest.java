package http;

import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeaders httpRequestHeaders;

    public HttpRequest(BufferedReader br) throws IOException {
        this.httpRequestLine = IOUtils.readRequestLine(br);
        this.httpRequestHeaders = IOUtils.readRequestHeaders(br);
    }

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }
}
