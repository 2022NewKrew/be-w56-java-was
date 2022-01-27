package webserver.response;

import java.nio.charset.StandardCharsets;

class ErrorResponse extends Response {

    ErrorResponse(StatusCode statusCode, String msg) {
        super(statusCode);
        setContents("text/plain;charset=utf-8", msg.getBytes(StandardCharsets.UTF_8));
    }
}
