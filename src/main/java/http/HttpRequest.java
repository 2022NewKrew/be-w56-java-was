package http;

import java.net.URI;

public class HttpRequest {
    private HttpMethod httpMethod;
    private HttpHeaders headers;
    private HttpRequestParams httpRequestParams;
    private URI requestUri;
    private Object body;

    public HttpRequest(HttpMethod httpMethod, URI requestUri, HttpRequestParams httpRequestParams, HttpHeaders headers, Object body) {
        this.httpMethod = httpMethod;
        this.httpRequestParams = httpRequestParams;
        this.requestUri = requestUri;
        this.headers = headers;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public HttpRequestParams getRequestParams() {
        return httpRequestParams;
    }

    public String getPath() {
        return requestUri.getPath();
    }

    public URI getRequestUri() {
        return requestUri;
    }

    public Object getBody() {
        return body;
    }
}
