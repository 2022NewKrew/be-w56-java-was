package http.response;

import http.ContentType;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private String header;

    public HttpResponseHeader(String url, int bodyLength) {
        header = "Content-Type: " + contentTypeOf(url) + ";charset=utf-8\r\n" +
                "Content-Length: " + bodyLength + "\r\n";
    }

    public void addKeyValue(String key, String value) {
        header += key + ": " + value + "\r\n";
    }

    public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
        try {
            dos.writeBytes(header + "\r\n");
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

    public String getHeader() { return header + "\r\n"; }
}
