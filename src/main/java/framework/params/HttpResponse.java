package framework.params;

import framework.constant.HttpStatusCode;
import lombok.Getter;
import lombok.Setter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Getter
@Setter
public class HttpResponse {
    private HttpStatusCode httpStatusCode;
    private String mimeType;
    private byte[] body;
    private Cookie cookie;
    private String redirectUrl;

    public HttpResponse(HttpStatusCode httpStatusCode, Session session) {
        this.httpStatusCode = httpStatusCode;
        this.mimeType = "text/html";
        this.cookie = new Cookie(session.getSession());
        this.body = new byte[0];
    }

    public DataOutputStream getResponse(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        setResponseHeader(dos);
        setResponseBody(dos);
        return dos;
    }

    private void setResponseHeader(DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("HTTP/1.1 %s %s\r\n", httpStatusCode.getStatusCode(), httpStatusCode.getMsg()));
        if (httpStatusCode.equals(HttpStatusCode.REDIRECT)) {
            dos.writeBytes(String.format("Location: %s\r\n", redirectUrl));
        }
        dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        if (!cookie.isEmpty()) {
            dos.writeBytes(String.format("Set-Cookie: %s; Path=/", cookie.toString()));
        }
        dos.writeBytes("\r\n");
    }

    private void setResponseBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }
}
