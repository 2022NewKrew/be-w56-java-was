package http.response;

import http.ContentType;
import http.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private final String header;
    private static final String HTTP_VERSION = "HTTP/1.1";

    public HttpResponseHeader(String url, int bodyLength) {
        header = HTTP_VERSION + " " + HttpStatus.OK + " \r\n" +
                "Content-Type: " + contentTypeOf(url) + ";charset=utf-8\r\n" +
                "Content-Length: " + bodyLength + "\r\n" +
                "\r\n";
    }

    public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
        try {
            dos.writeBytes(header);
        } catch (IOException e) {
            throw new IOException("Failed to write response header to dos");
        }
    }

    private String contentTypeOf(String url) {
        String[] tokens = url.split("\\.");
        String extension = tokens[tokens.length - 1];

        switch (extension) {
            case "css": return ContentType.CSS;
            case "js": return ContentType.JS;
            case "html": return ContentType.HTML;
            default: return ContentType.DEFAULT;
        }
    }
}
