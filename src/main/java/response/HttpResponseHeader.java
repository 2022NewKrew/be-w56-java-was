package response;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private final String header;

    public HttpResponseHeader(String url, int bodyLength) {
        header = "HTTP/1.1 200 OK \r\n" +
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
            case "css": return ContentType.CSS.value();
            case "js": return ContentType.JS.value();
            case "html": return ContentType.HTML.value();
            default: return ContentType.DEFAULT.value();
        }
    }
}
