package http.request;

import http.HttpHeaders;
import http.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private final RequestBody body;

    private HttpRequest(RequestLine requestLine, HttpHeaders headers, RequestBody body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(BufferedReader br) throws IOException {
        String requestLineString = IOUtils.readRequestLine(br);
        RequestLine requestLine = HttpRequestUtils.parseRequestLine(requestLineString);

        List<String> headerStrings = IOUtils.readHttpHeaders(br);
        HttpHeaders headers = HttpRequestUtils.parseHeaders(headerStrings);

        int contentLength = headers.getContentLength();
        if (contentLength == 0) {
            return new HttpRequest(requestLine, headers, RequestBody.empty());
        }

        String bodyString = IOUtils.readData(br, contentLength);
        MediaType contentType = headers.getContentType();
        RequestBody body = HttpRequestUtils.parseRequestBody(contentType, bodyString);

        return new HttpRequest(requestLine, headers, body);
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestBody getBody() {
        return body;
    }
}
