package dto;

import collections.ResponseHeaders;

public class Response {

    private ResponseHeaders headers;
    private byte[] body;

    public Response(ResponseHeaders headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    public ResponseHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

}
