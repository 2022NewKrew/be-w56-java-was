package http;

import util.HttpRequestUtils;

import java.net.URI;

public class HttpRequest {
    private HttpMethod httpMethod;
    private HttpHeaders headers;
    private HttpRequestParams httpRequestParams;
    private URI requestUri;
    private String body;

    public HttpRequest(HttpMethod httpMethod, URI requestUri, HttpRequestParams httpRequestParams, HttpHeaders headers, String body) {
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.headers = headers;
        this.body = body;
        if(hasContentType() && getContentType().equals(ContentType.APPLICATION_X_WWW_FORM_URLENCODED)) {
            httpRequestParams.putAll(HttpRequestUtils.parseQueryString(body));
        }
        this.httpRequestParams = httpRequestParams;
    }

    public ContentType getContentType() {
        return ContentType.getContentType(headers.getHeaderByName("Content-Type"));
    }

    public boolean hasContentType() {
        return headers.getHeaderByName("Content-Type") != null;
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
