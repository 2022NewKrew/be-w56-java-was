package util.response;

import util.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    private HttpStatus httpStatus;
    private final Map<String, String> header = new HashMap<>();
    private byte[] body;

    public ResponseBuilder setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ResponseBuilder addHeader(String key, String value) {
        header.put(key, value);
        return this;
    }

    public ResponseBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public Response build() {
        return new Response(httpStatus,header,body);
    }
}
