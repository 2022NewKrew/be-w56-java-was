package webserver.requesthandler.httpresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import webserver.common.util.HttpUtils;
import common.controller.ControllerResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@AllArgsConstructor
public class HttpResponse {
    private final HttpResponseStartLine startLine;
    private final List<String> header;
    private final byte[] body;

    public static HttpResponse valueOf(ControllerResponse controllerResponse, String httpVersion) throws IOException {
        String redirectTo = controllerResponse.getRedirectTo();
        log.debug("[Redirect]: " + redirectTo);

        Map<String, String> mappedHeader = controllerResponse.getHeader();
        HttpStatus httpStatus = controllerResponse.getHttpStatus();
        byte[] body = Files.readAllBytes(new File(redirectTo).toPath());
        mappedHeader.put("Content-Length", String.valueOf(body.length));

        List<String> header = HttpUtils.mappedHeaderToList(mappedHeader);
        HttpResponseStartLine startLine = new HttpResponseStartLine(httpVersion, httpStatus);
        return new HttpResponse(startLine, header, body);
    }

    public void respond(OutputStream out) {
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
