package response;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseHeader {
    private final String header;

    public HttpResponseHeader(String contentType, int bodyLength) {
        header = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: " + contentType + ";charset=utf-8\r\n" +
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
}
