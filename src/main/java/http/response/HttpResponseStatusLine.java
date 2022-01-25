package http.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseStatusLine {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private final String statusLine;

    public HttpResponseStatusLine(String status) {
        statusLine = HTTP_VERSION + " " + status + " \r\n";
    }

    public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
        try {
            dos.writeBytes(statusLine);
        } catch (IOException e) {
            throw new IOException("Failed to write response status line to dos");
        }
    }
}
