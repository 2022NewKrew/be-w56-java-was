package http.request;

import http.header.HttpHeaders;

public class HttpRequest {
    private HttpRequestLine requestLine;
    private HttpHeaders headers;
    private HttpRequestBody body;

    public HttpRequest(HttpRequestLine requestLine, HttpHeaders requestHeader, HttpRequestBody requestBody) {
        this.requestLine = requestLine;
        this.headers = requestHeader;
        this.body = requestBody;
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

    public String getParam(String param) {
        return requestLine.containsParam(param) ? requestLine.getParam(param) : body.getParam(param);
    }
    public void setHeader(String header, String value) {
        headers.add(header, value);
    }
}
