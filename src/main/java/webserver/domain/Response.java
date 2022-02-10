package webserver.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.Builder;

public class Response {

    private static final String RESPONSE_HEADER_FORMAT = "%s%s\r\n";
    private static final int LENGTH_OF_BODY_IS_NULL = 0;

    private final StatusLine statusLine;
    private final Headers headers;
    private final byte[] body;

    @Builder
    private Response(StatusLine statusLine, Headers headers, byte[] body) {
        this.statusLine = new StatusLine(statusLine);
        this.headers = new Headers(headers);
        this.body = body;
    }

    public static Response createResponse(HttpStatus httpStatus, byte[] body, String contentTypes) {
        StatusLine statusLine = StatusLine.createStatus(httpStatus);
        Headers headers = (body == null) ? Headers.createResponseHeader(LENGTH_OF_BODY_IS_NULL, contentTypes)
            : Headers.createResponseHeader(body.length, contentTypes);
        return Response.builder()
            .statusLine(statusLine)
            .headers(headers)
            .body(body)
            .build();
    }

    public static Response createResponse(HttpStatus httpStatus, String redirect) {
        StatusLine statusLine = StatusLine.createStatus(httpStatus);
        Headers headers = Headers.createResponseHeader(redirect);
        return Response.builder()
            .statusLine(statusLine)
            .headers(headers)
            .build();
    }

    public Response setCookie(String cookie) {
        headers.setCookie(cookie);
        return this;
    }

    public byte[] getBytes() throws IOException {
        String header = String.format(RESPONSE_HEADER_FORMAT, statusLine, headers);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(header.getBytes());
        if (body != null) {
            baos.write(body);
        }

        return baos.toByteArray();
    }
}
