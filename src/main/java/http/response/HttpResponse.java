package http.response;

import http.HttpBody;
import http.HttpHeader;
import http.HttpStatusCode;
import http.util.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class HttpResponse {
    private final OutputStream out;
    private String httpVersion;
    private HttpStatusCode statusCode;
    private HttpHeader header = new HttpHeader();
    private byte[] body;

    public HttpResponse(OutputStream out) {
        this.out = out;
    }

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

    public void send() throws IOException {
        IOUtils.writeData(new DataOutputStream(out), this);
    }
}
