package webserver.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.Builder;

public class Response {

    private final static String RESPONSE_HEADER_FORMAT = "%s%s\r\n";

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
        Headers headers = Headers.createResponseHeader(body.length, contentTypes);
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
