package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import util.Constant;

// HttpResponse 대체 예정
public class NewHttpResponse {

    private final ResponseStatusLine statusLine;
    private final ResponseHeader header;
    private final ResponseBody body;
    private final DataOutputStream dos;

    public NewHttpResponse(ResponseStatusLine statusLine, ResponseHeader header,
            ResponseBody body, DataOutputStream dos) {
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
        this.dos = dos;
    }

    public void sendResponse() throws IOException {
        sendStartLine();
        sendHeader();
        dos.writeBytes(Constant.lineBreak);
        sendBody();
        dos.flush();
    }

    private void sendStartLine() throws IOException {
        dos.writeBytes(statusLine.getLine());
    }

    private void sendHeader() throws IOException {
        dos.writeBytes(header.getComponentString());
    }

    private void sendBody() throws IOException {
        dos.write(body.getBody());
    }
}
