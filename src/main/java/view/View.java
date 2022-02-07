package view;

import lombok.Getter;
import lombok.Setter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * View를 FrontController에 송신
 */
@Getter
@Setter
public class View {
    private String mimeType;
    private byte[] body;

    public DataOutputStream getResponse(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        setResponse200Header(dos);
        setResponseBody(dos);
        return dos;
    }

    private void setResponse200Header(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void setResponseBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }
}
