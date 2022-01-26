package util;

import org.apache.tika.Tika;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Tika tika = new Tika();
    private static final byte BYTE_TOKEN_SPACE = 0x20;
    private static final byte BYTE_TOKEN_COLON = 0x3A;
    private static final byte[] BYTE_TOKEN_CRLF = {0x0D, 0x0A};

    private final Charset ENCODING;
    private final String HTTP_VERSION;
    private final Map<String, String> headers = new HashMap<>();
    private HttpStatus httpStatus;
    private byte[] body;

    public HttpResponse(String HTTP_VERSION, Charset ENCODING) {
        this.HTTP_VERSION = HTTP_VERSION;
        this.ENCODING = ENCODING;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setContentTypeWithURI(String uri) {
        String contentType = tika.detect(uri);
        headers.put("Content-type", contentType + ";charset=" + ENCODING.name());
    }

    public void setBody(byte[] body) {
        this.body = body;
        headers.put("Content-Length", String.valueOf(body.length));
    }

    public byte[] toByte() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Status Line
        baos.write(HTTP_VERSION.getBytes());
        baos.write(BYTE_TOKEN_SPACE);
        baos.write(httpStatus.getStatusCode());
        baos.write(BYTE_TOKEN_SPACE);
        baos.write(httpStatus.name().getBytes());
        baos.write(BYTE_TOKEN_CRLF);

        // Headers
        for(var header : headers.entrySet()) {
            baos.write(header.getKey().getBytes());
            baos.write(BYTE_TOKEN_COLON);
            baos.write(header.getValue().getBytes());
            baos.write(BYTE_TOKEN_CRLF);
        }
        baos.write(BYTE_TOKEN_CRLF);

        // Body
        if (body != null) {
            baos.writeBytes(body);
        }
        return baos.toByteArray();
    }
}
