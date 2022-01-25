package http;

public class HttpResponse {
    private HttpHeaders headers;
    private StatusCode statusCode;
    private Object body;

    public HttpResponse(HttpHeaders httpHeaders, StatusCode statusCode, Object body) {
        this.headers = httpHeaders;
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void addHeader(String headerName, String headerValue) {
        headers.addHeader(headerName, headerValue);
    }

    public Object getResponseBody() {
        return body;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
