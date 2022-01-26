package util.response;

import util.HttpStatus;

import java.util.Map;

public class Response {
    private final HttpStatus httpStatus;
    private final Map<String, String> header;
    private final byte[] body;

    public Response(HttpStatus httpStatus, Map<String, String> header, byte[] body) {
        this.httpStatus = httpStatus;
        this.header = header;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
