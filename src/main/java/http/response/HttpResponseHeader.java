package http.response;

import http.ContentType;
import http.ContentTypeMap;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private final HttpResponseStatusLine statusLine;
    private String headers;

    public HttpResponseHeader(String url, String status, int bodyLength) {
        statusLine = new HttpResponseStatusLine(status);
        headers = "Content-Type: " + contentTypeOf(url).str() + ";charset=utf-8\r\n" +
                "Content-Length: " + bodyLength + "\r\n";
    }

    public void putToHeaders(String key, String value) {
        headers += key + ": " + value + "\r\n";
    }

    public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
        try {
            statusLine.writeToDataOutputStream(dos);
            dos.writeBytes(headers + "\r\n");
        } catch (IOException e) {
            throw new IOException("Failed to write response header to dos");
        }
    }

    private ContentType contentTypeOf(String url) {
        String[] tokens = url.split("\\.");
        String extension = tokens[tokens.length - 1];

        return ContentTypeMap.get(extension);
    }

    public String getHeaders() { return headers + "\r\n"; }
}
