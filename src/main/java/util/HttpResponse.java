package util;

import org.apache.tika.Tika;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Tika tika = new Tika();
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private final String httpVersion;
    private final Map<String, String> headers = new HashMap<>();
    private HttpStatus httpStatus;
    private byte[] body;

    public HttpResponse(String httpVersion) {
        this.httpVersion = httpVersion;
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
        // https://datatracker.ietf.org/doc/html/rfc7230#section-3.1.2
        // status-line = HTTP-version SP status-code SP reason-phrase CRLF
        baos.write(httpVersion.getBytes());
        baos.write(0x20); // Space
        baos.write(httpStatus.getStatusCode());
        baos.write(0x20); // Space
        baos.write(httpStatus.name().getBytes());
        baos.write(0x20); // Space
        baos.write(0x0d); // CR
        baos.write(0x0a); // LF

        // Headers
        // https://datatracker.ietf.org/doc/html/rfc7230#section-3.2
        // header-field = field-name ":" OWS field-value OWS
        for(var header : headers.entrySet()) {
            baos.write(header.getKey().getBytes());
            baos.write(0x3A); // Colon
            baos.write(header.getValue().getBytes());
            baos.write(0x0d); // CR
            baos.write(0x0a); // LF
        }
        baos.write(0x0d); // CR
        baos.write(0x0a); // LF

        // Body
        if (body != null) {
            baos.writeBytes(body);
        }
        return baos.toByteArray();
    }
}
