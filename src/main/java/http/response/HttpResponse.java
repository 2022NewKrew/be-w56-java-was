package http.response;

import http.HttpHeaders;
import http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final StatusLine statusLine;

    private final HttpHeaders headers;

    private final ResponseBody body;

    private HttpResponse(StatusLine statusLine, HttpHeaders headers, ResponseBody body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public  HttpResponse(Builder builder) {
        this.statusLine = builder.statusLine;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public static HttpResponse of(String protocolVersion, HttpStatus httpStatus, Map<String, String> headerMap, byte[] body) {
        return new HttpResponse(StatusLine.of(protocolVersion, httpStatus), HttpHeaders.of(headerMap), ResponseBody.of(body));
    }

    public ResponseBody getBody() {
        return this.body;
    }

    public String getProtocolVersion() {
        return this.statusLine.getProtocolVersion();
    }

    public String getStatusCode() {
        return this.statusLine.getStatusCode();
    }

    public String getStatusText() {
        return this.statusLine.getStatusText();
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public ResponseBody getResponseBody() {
        return body;
    }

    public static class Builder {

        private StatusLine statusLine;
        private HttpHeaders headers;
        private ResponseBody body;

        public static Builder newInstance()
        {
            return new Builder();
        }

        private Builder() {}

        public Builder statusLine(StatusLine statusLine)
        {
            this.statusLine = statusLine;
            return this;
        }
        public Builder headers(HttpHeaders headers)
        {
            this.headers = headers;
            return this;
        }
        public Builder body(ResponseBody body)
        {
            this.body = body;
            return this;
        }

        public HttpResponse build()
        {
            return new HttpResponse(this);
        }
    }
}
