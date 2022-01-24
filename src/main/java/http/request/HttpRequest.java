package http.request;

import http.HttpHeaders;

import java.util.List;

public class HttpRequest {
    private HttpRequestLine requestLine;
    private HttpHeaders headers;
    private HttpRequestBody body;

    public HttpRequest(String requestLine, List<String> requestHeader, List<String> requestBody) {
        this.requestLine = new HttpRequestLine(requestLine);
        this.headers = new HttpHeaders(requestHeader);
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getUrl() {
        return requestLine.getUrl();
    }

    public String getHeader(String header) {
        return headers.getFirst(header);
    }

    public void setHeader(String header, String value) {
        headers.add(header, value);
    }
}
