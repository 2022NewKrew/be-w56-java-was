package http.impl;

import http.HttpHeaders;
import http.HttpMethod;
import http.HttpRequest;

import java.net.URI;

class HttpRequestImpl implements HttpRequest  {

    private HttpMethod httpMethod;
    private HttpHeaders headers;
    private URI requestUri;

    public HttpRequestImpl(HttpMethod httpMethod, URI requestUri, HttpHeaders headers) {
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.headers = headers;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    @Override
    public HttpMethod getMethod() {
        return httpMethod;
    }

    @Override
    public URI getRequestUri() {
        return requestUri;
    }

    @Override
    public String getBody() {
        return null;
    }
}
