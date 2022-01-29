package http;

import util.StringUtils;

import java.net.URI;
import java.util.List;

public class HttpRequest {
    private HttpMethod httpMethod;
    private HttpHeaders headers;
    private URI requestUri;
    private byte[] body;

    public HttpRequest(HttpMethod httpMethod, URI requestUri, HttpHeaders headers, byte[] body) {
        this.httpMethod = httpMethod;
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

    public List<String> getAccepts() {
        return StringUtils.parseString(headers.getHeaderByName("Accept"), ",");
    }

    public boolean equalsMethod(HttpMethod httpMethod) {
        return this.httpMethod.equals(httpMethod);
    }

    public String getPath() {
        return requestUri.getPath();
    }

    public String getQuery() {
        return requestUri.getQuery();
    }

    public URI getRequestUri() {
        return requestUri;
    }

    public boolean hasBody() {
        return body != null;
    }

    public ContentType getContentType() {
        return ContentType.getContentType(headers.getHeaderByName("Content-Type"));
    }

    public byte[] getBody() {
        return body;
    }
}
