package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpMethod;
import webserver.enums.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String DEFAULT_PATH = "./webapp";

    private final String statusLine;
    private byte[] body;

    public HttpResponse(HttpRequest httpRequest) throws IOException {
        HttpStatus httpStatus = HttpStatus.OK;

        if (httpRequest.getMethod() == HttpMethod.GET) {
            try {
                body = Files.readAllBytes(new File(DEFAULT_PATH + httpRequest.getUri()).toPath());
                httpStatus = HttpStatus.OK;
            } catch (IOException e) {
                httpStatus = HttpStatus.NOT_FOUND;
            }
        }
        else if (httpRequest.getMethod() == HttpMethod.POST) {
            httpStatus = HttpStatus.CREATED;
        }

        statusLine = httpStatus.makeStatusLine(httpRequest.getVersion());
    }

    public String getStatusLine() {
        return statusLine;
    }

    public byte[] getBody() {
        return body;
    }
}
