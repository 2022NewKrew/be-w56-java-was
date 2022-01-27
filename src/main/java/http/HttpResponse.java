package http;

public class HttpResponse {
    private HttpHeaders headers;
    private StatusCode statusCode;
    private byte[] body;

    public HttpResponse(HttpHeaders httpHeaders, StatusCode statusCode, byte[] body) {
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

    public byte[] getResponseBody() {
        return body;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
