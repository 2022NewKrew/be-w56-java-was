package webserver.requesthandler.httpresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import webserver.common.util.HttpUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@AllArgsConstructor
public class HttpResponse {
    private final HttpResponseStartLine startLine;
    private final List<String> header;
    private final byte[] body;

    public static HttpResponse valueOf(HttpStatus httpStatus, Map<String, String> mappedHeader, String httpVersion, byte[] body) {
        mappedHeader.put("Content-Length", String.valueOf(body.length));

        List<String> header = HttpUtils.mappedHeaderToList(mappedHeader);
        HttpResponseStartLine startLine = new HttpResponseStartLine(httpVersion, httpStatus);
        return new HttpResponse(startLine, header, body);
    }

    public void doResponse(OutputStream out) {
        log.debug("[HTTP Response]");
        DataOutputStream dos = new DataOutputStream(out);
        outputHeader(dos);
        outputBody(dos);
    }

    private void outputHeader(DataOutputStream dos) {
        try {
            dos.writeBytes(this.startLine.toString());
            for (String s : header) {
                dos.writeBytes(s);
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void outputBody(DataOutputStream dos) {
        try {
            dos.write(this.body, 0, this.body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
