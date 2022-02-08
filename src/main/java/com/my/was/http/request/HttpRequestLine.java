package com.my.was.http.request;

public class HttpRequestLine {

    private HttpMethod method;
    private HttpRequestUri requestUri;
    private String httpVersion;

    public HttpRequestLine(String requestLine) {
        String[] parsedRequestLine = requestLine.split(" ");
        method = HttpMethod.getHttpMethodByMethodName(parsedRequestLine[0]);
        requestUri = new HttpRequestUri(parsedRequestLine[1]);
        httpVersion = parsedRequestLine[2];
    }

    public String getPath() {
        return requestUri.getPath();
    }

    public String getParam(String paramName) {
        return requestUri.getParam(paramName);
    }

    public boolean isMethod(HttpMethod method) {
        return this.method == method;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
