package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Response;
import webserver.http.response.HttpResponse;
import webserver.response.maker.ResponseMaker;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private final DataOutputStream dos;
    private final String httpVersion;

    public ResponseHandler(DataOutputStream dos, String httpVersion) {
        this.dos = dos;
        this.httpVersion = httpVersion;
    }

    public void response(Response result) throws IOException {
        HttpResponse response = ResponseMaker.make(result, httpVersion);
        responseOutput(response);
    }

    private void responseOutput(HttpResponse response) {
        try {
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody(), 0, response.bodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
