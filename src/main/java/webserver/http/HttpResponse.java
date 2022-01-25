package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponse {
    private static final String URI_BASE = "./webapp";
    private static final String CRLF = "\r\n";
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private String version;
    private String uri;
    private HttpStatus statusCode;
    private String contentType;

    public String getVersion() {
        return version;
    }

    public String getUri() {
        return uri;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void response(DataOutputStream dos) {
        try {
            byte[] body = Files.readAllBytes(new File(URI_BASE + uri).toPath());
            responseHeader(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes(version + " " + statusCode + CRLF);
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8" + CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
