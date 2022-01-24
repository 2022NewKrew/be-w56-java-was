package http.request;

import http.HttpHeaders;

import java.util.List;

public class HttpRequest {
    private HttpRequestLine requestLine;
    private HttpHeaders headers;
    private HttpRequestBody body;

    public HttpRequest(String requestLine, List<String> requestHeader, List<String> requestBody) {
        this.requestLine = new HttpRequestLine(requestLine);
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getUrl() {
        return requestLine.getUrl();
    }
}
