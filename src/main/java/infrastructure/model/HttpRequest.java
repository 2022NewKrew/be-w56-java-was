package infrastructure.model;

import java.util.Objects;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeader httpHeader;
    private HttpBody httpBody;

    public HttpRequest(RequestLine requestLine, HttpHeader httpHeader) {
        this.requestLine = requestLine;
        this.httpHeader = httpHeader;
    }

    public HttpRequest(RequestLine requestLine, HttpHeader httpHeader, HttpBody httpBody) {
        this.requestLine = requestLine;
        this.httpHeader = httpHeader;
        this.httpBody = httpBody;
    }

    public Path getRequestPath() {
        return requestLine.getPath();
    }

    public HttpBody getRequestBody() {
        return httpBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return Objects.equals(requestLine, that.requestLine) && Objects.equals(httpHeader, that.httpHeader) && Objects.equals(httpBody, that.httpBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestLine, httpHeader, httpBody);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestLine=" + requestLine +
                ", requestHeader=" + httpHeader +
                ", requestBody=" + httpBody +
                '}';
    }
}
