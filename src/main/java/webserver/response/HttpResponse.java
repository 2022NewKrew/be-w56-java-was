package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private HttpRequest httpRequest;
    private int statusCode;
    private String mimeType;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body = new byte[0];

    public HttpResponse(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public byte[] toByte() {
        byte[] header = HttpResponseBuilder.responseHeader(this);
        byte[] response = new byte[header.length + body.length];
        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);
        return response;
    }

}
