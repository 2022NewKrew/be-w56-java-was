package bin.jayden.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyHttpResponse {
    private static final byte[] CLRF = "\r\n".getBytes(StandardCharsets.UTF_8);
    private final byte[] body;
    private final HttpStatusCode statusCode;
    private final Map<String, String> addedHeader;
    private final Mime mime;

    private MyHttpResponse(byte[] body, HttpStatusCode statusCode, Mime mime, Map<String, String> header) {
        this.body = body;
        this.statusCode = statusCode;
        this.mime = mime;
        this.addedHeader = header;
    }

    public byte[] getBody() {
        return body;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public byte[] toBytes() {
        String header = getHeader(body.length + CLRF.length);
        ByteBuffer byteBuffer = ByteBuffer.allocate(header.length() + body.length + CLRF.length);
        byteBuffer.put(header.getBytes());
        byteBuffer.put(body);
        byteBuffer.put(CLRF);
        return byteBuffer.array();
    }


    private String getHeader(int lengthOfBodyContent) {
        StringBuilder header = new StringBuilder("HTTP/1.1 " + statusCode + " \r\n");
        for (Map.Entry<String, String> entry : addedHeader.entrySet()) {
            header.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        header.append("Content-Type: ").append(mime.getContentType()).append(";charset=utf-8\r\n").append("Content-Length: ").append(lengthOfBodyContent).append("\r\n").append("\r\n");
        return header.toString();
    }

    public static class Builder {
        private final Map<String, String> header = new HashMap<>();
        private byte[] body = new byte[0];
        private HttpStatusCode statusCode = HttpStatusCode.STATUS_CODE_200;
        private Mime mime = Mime.HTML;

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder setBody(String body) {
            return setBody(body.getBytes(StandardCharsets.UTF_8));
        }

        public Builder addHeader(String key, String value) {
            header.put(key, value);
            return this;
        }

        public Builder setStatusCode(HttpStatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setMime(Mime mime) {
            this.mime = mime;
            return this;
        }

        public MyHttpResponse build() {
            return new MyHttpResponse(body, statusCode, mime, header);
        }
    }
}
