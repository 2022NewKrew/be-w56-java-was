package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final DataOutputStream dos;
    private final byte[] body;
    private final String version;
    private final HttpStatus status;
    private final Map<String, String> headers;
    private final String contentType;
    private final int lengthOfBodyContent;

    public Response(DataOutputStream dos, byte[] body, HttpStatus status,
                    Map<String, String> headers, String contentType) {
        this.dos = dos;
        this.body = body;
        this.version = "HTTP/1.1";
        this.status = status;
        this.headers = headers;
        this.contentType = contentType;
        this.lengthOfBodyContent = body.length;
    }

    public void write() {
        try {
            dos.writeBytes(String.format("%s %s \r\n", version, status));
            writeBytesHeaders();
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
            dos.writeBytes(String.format("Content-Length: %d \r\n", lengthOfBodyContent));
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void writeBytesHeaders() throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(String.format("%s: %s \r\n", key, headers.get(key)));
        }
    }

}
