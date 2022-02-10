package model;

import java.io.IOException;
import java.util.Map;

public class HttpClientErrorResponse extends HttpResponse {

    public static HttpClientErrorResponse of(HttpStatus httpStatus, byte[] body) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1, httpStatus.getCode(),
                httpStatus.getMessage());
        Map<HttpHeader, String> headerKeyMap = Map.of(
                HttpHeader.CONTENT_TYPE, Mime.HTML.getType(),
                HttpHeader.CONTENT_LENGTH, Integer.toString(body.length)
        );
        return new HttpClientErrorResponse(statusLine, new Header(headerKeyMap), body);
    }

    private final byte[] body;

    public HttpClientErrorResponse(StatusLine statusLine, Header header, byte[] body) {
        super(statusLine, header);
        this.body = body;
    }

    @Override
    public byte[] message() {
        byte[] header = headerMessage();
        byte[] message = new byte[header.length + body.length];
        System.arraycopy(header, 0, message, 0, header.length);
        System.arraycopy(body, 0, message, header.length, body.length);
        return message;
    }
}
