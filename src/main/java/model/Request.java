package model;

import java.util.Map;

public class Request {
    private final RequestLine requestLine;
    private final RequestHeaders requestHeaders;
    private final RequestBodies requestBodies;

    private Request(RequestLine requestLine, RequestHeaders requestHeaders, RequestBodies requestBodies) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBodies = requestBodies;
    }

    public static Request of(RequestLine requestLine, RequestHeaders requestHeaders, RequestBodies requestBodies) {
        return new Request(requestLine, requestHeaders, requestBodies);
    }

    public String getAcceptType() {
        return requestHeaders.getAcceptType(requestLine.getUrl());
    }

    public String getUrl() {
        return requestLine.getUrl();
    }

    public String getRequestMethod() {
        return requestLine.getMethod();
    }

    public String getRequestBody(String key) {
        return requestBodies.getRequestBody(key);
    }

    public boolean isFile() {
        return requestLine.isFile();
    }

    public boolean isEqualUrl(String url) {
        return requestLine.isEqualUrl(url);
    }

    public Map<String, String> getQueryStrings() {
        return requestLine.getQueryStrings();
    }
}
