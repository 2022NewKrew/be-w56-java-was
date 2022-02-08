package model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import view.View;

public class HttpSuccessfulResponse extends HttpResponse {

    public static HttpSuccessfulResponse of(HttpStatus httpStatus, String url) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        byte[] body = View.get(url);
        Map<String, String> headerKeyMap = Map.of(
                "Content-Type", getContentType(url),
                "Content-Length", Integer.toString(body.length)
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

    public HttpSuccessfulResponse(StatusLine statusLine, Header header, byte[] body) {
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
