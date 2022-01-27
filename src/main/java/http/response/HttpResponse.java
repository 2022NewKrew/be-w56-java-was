package http.response;

import http.HttpBody;
import http.HttpHeader;
import http.HttpStatusCode;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class HttpResponse {
    private String httpVersion;
    private HttpStatusCode statusCode;
    private HttpHeader header;
    private byte[] body;

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(HttpHeader header) {
        this.header = header;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public HttpHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
