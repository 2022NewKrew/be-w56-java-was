package http.response;

import http.HttpStatus;

import java.util.Map;

public class HttpResponse {

    private StatusLine statusLine;

    private ResponseHeaders headers;

    private ResponseBody body;

    private HttpResponse(StatusLine statusLine, ResponseHeaders headers, ResponseBody body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse create() {
        return new HttpResponse(new StatusLine(), new ResponseHeaders(), new ResponseBody());
    }

    public void addHeader(String key, String value) {
        headers.addHeader(key, value);
    }

    public void updateProtocolVersion(String protocolVersion) {
        statusLine.updateProtocolVersion(protocolVersion);
    }

    public void updateStatus(HttpStatus status) {
        statusLine.updateStatusCode(status.getCode());
        statusLine.updateStatusText(status.getText());
    }

    public void updateBody(byte[] body) {
        this.body.updateBody(body);
    }

    public byte[] getBody() {
        return this.body.getBody();
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

    public Map<String, String> getHeaders() {
        return this.headers.getHeaders();
    }
}
