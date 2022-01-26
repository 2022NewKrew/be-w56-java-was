package bin.jayden.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MyHttpResponse {
    private static final byte[] CLRF = "\r\n".getBytes(StandardCharsets.UTF_8);
    private final byte[] body;
    private final HttpStatusCode statusCode;
    private final Mime mime;
    private final String location;

    private MyHttpResponse(byte[] body, HttpStatusCode statusCode, Mime mime, String location) {
        this.body = body;
        this.statusCode = statusCode;
        this.mime = mime;
        this.location = location;
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
        String header = "HTTP/1.1 " + statusCode + " \r\n";

        if (statusCode == HttpStatusCode.STATUS_CODE_302) {
            header += "Location: " + location + "\r\n" +
                    "\r\n";
        } else {
            header += "Content-Type: " + mime.getContentType() + ";charset=utf-8\r\n" +
                    "Content-Length: " + lengthOfBodyContent + "\r\n" +
                    "\r\n";
        }
        return header;
    }

    public static class Builder {
        private byte[] body = new byte[0];
        private HttpStatusCode statusCode = HttpStatusCode.STATUS_CODE_200;
        private Mime mime = Mime.HTML;
        private String location = null;

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder setBody(String body) {
            return setBody(body.getBytes(StandardCharsets.UTF_8));
        }

        public Builder setLocation(String location) {
            this.location = location;
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
            return new MyHttpResponse(body, statusCode, mime, location);
        }
    }
}
