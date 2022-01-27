package dto;

import collections.ResponseHeaders;

public class Response {

    private String statusLine;
    private ResponseHeaders headers;
    private byte[] body;

    public Response(String statusLine, ResponseHeaders headers, byte[] body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public ResponseHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String getStatusLine() {
        return statusLine;
    }
}
