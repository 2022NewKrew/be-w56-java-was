package infrastructure.util;

import infrastructure.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private ResponseHandler() {
    }

    public static void response(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(HttpResponseUtils.convertResponseStartLine(response.getResponseLine()));
            dos.writeBytes(HttpResponseUtils.convertHeader(response.getHttpHeader()));
            dos.writeBytes("\r\n");
            if (response.getResponseBody() != null) {
                dos.writeBytes("\r\n");
                dos.write(response.getResponseBody().getValue());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
