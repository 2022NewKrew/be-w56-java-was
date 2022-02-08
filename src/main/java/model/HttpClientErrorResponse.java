package model;

import java.io.IOException;
import java.util.Map;
import view.View;

public class HttpClientErrorResponse extends HttpResponse {

    public static HttpClientErrorResponse of(HttpStatus httpStatus, String url) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        byte[] body = View.get(url);
        Map<String, String> headerKeyMap = Map.of(
                "Content-Type", Mime.HTML.getType(),
                "Content-Length", Integer.toString(body.length)
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
