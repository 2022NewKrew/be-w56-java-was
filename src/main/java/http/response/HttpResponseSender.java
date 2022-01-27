package http.response;

import http.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpResponseSender {

    private final Logger log = LoggerFactory.getLogger(HttpResponseSender.class);
    private final HttpResponse httpResponse;
    private DataOutputStream dos;

    public HttpResponseSender(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public void send(OutputStream outputStream) {
        dos = new DataOutputStream(outputStream);
        writeHeader();
        writeBody();
    }

    private void writeHeader() {
        try {
            writeResponseLine();
            writeResponseHeader();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeResponseHeader() throws IOException {
        Map<String, String> headers = httpResponse.getHeaders();
        for (Map.Entry header : headers.entrySet()) {
            dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
        }
        dos.writeBytes("\r\n");
    }

    private void writeResponseLine() throws IOException {
        HttpStatusCode httpStatusCode = httpResponse.getHttpStatusCode();
        dos.writeBytes(String.format("HTTP/1.1 %s %s\r\n", httpStatusCode.getStatusCode(), httpStatusCode.getStatusPhrase()));
    }

    private void writeBody() {
        Resource httpResponseBody = httpResponse.getHttpResponseBody();
        try {
            dos.write(httpResponseBody.getContent(), 0, httpResponseBody.getContent().length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
