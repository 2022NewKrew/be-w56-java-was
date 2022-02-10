package model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpSuccessfulResponse extends HttpResponse {

    public static HttpSuccessfulResponse of(HttpStatus httpStatus, String url, byte[] body) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1, httpStatus.getCode(),
                httpStatus.getMessage());
        Map<HttpHeader, String> headerKeyMap = Map.of(
                HttpHeader.CONTENT_TYPE, getContentType(url),
                HttpHeader.CONTENT_LENGTH, Integer.toString(body.length)
        );
        return new HttpSuccessfulResponse(statusLine, new Header(headerKeyMap), body);
    }

    private static String getContentType(String path) {
        List<String> splitResult = List.of(path.split("\\."));
        int length = splitResult.size();
        String extension = splitResult.get(length - 1);

        return Mime.getMime(extension);
    }

    private final byte[] body;

    private HttpSuccessfulResponse(StatusLine statusLine, Header header, byte[] body) {
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
