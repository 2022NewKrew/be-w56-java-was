package framework.http.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponse {
    private static final String WEBAPP_PATH = "./webapp";

    public static final String DEFAULT_PROTOCOL = "HTTP/1.1";

    private StatusLine statusLine;
    private HttpResponseHeader header;
    private byte[] body;

    public HttpResponse(HttpStatus httpStatus, HttpResponseHeader header) {
        this.statusLine = new StatusLine(DEFAULT_PROTOCOL, httpStatus);
        this.header = header;
        this.body = new byte[1];
    }

    public HttpResponse(HttpStatus httpStatus, HttpResponseHeader header, String filePath) throws IOException {
        this.statusLine = new StatusLine(DEFAULT_PROTOCOL, httpStatus);
        this.header = header;
        this.body = readFileByBytes(filePath);
    }

    public HttpResponse(HttpStatus httpStatus, HttpResponseHeader header, byte[] body) {
        this.statusLine = new StatusLine(DEFAULT_PROTOCOL, httpStatus);
        this.header = header;
        this.body = body;
    }

    public HttpResponse(String protocol, HttpStatus httpStatus, HttpResponseHeader header) {
        this.statusLine = new StatusLine(protocol, httpStatus);
        this.header = header;
        this.body = new byte[1];
    }

    public HttpResponse(String protocol, HttpStatus httpStatus, HttpResponseHeader header, byte[] body) {
        this.statusLine = new StatusLine(protocol, httpStatus);
        this.header = header;
        this.body = body;
    }

    private byte[] readFileByBytes(String path) throws IOException {
        File file = new File(WEBAPP_PATH + path);
        byte[] bytes = Files.readAllBytes(file.toPath());

        return bytes;
    }

    public HttpResponseHeader getHeader() {
        return header;
    }

    public String getStatusLineText() {
        return statusLine.getStatusLineText();
    }

    public int getBodyLength() {
        return body.length;
    }

    public byte[] getBody() {
        return body;
    }
}
