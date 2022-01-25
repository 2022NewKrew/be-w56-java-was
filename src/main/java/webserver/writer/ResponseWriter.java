package webserver.writer;

import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class ResponseWriter {

    private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    public static void write(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getProtocolVersion() + " " + response.getStatusCode() + " " + response.getStatusText() + "\r\n");
            Map<String, String> headers = response.getHeaders();
            for(Map.Entry<String, String> header : headers.entrySet()) {
                dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
            }
            dos.writeBytes("\r\n");

            byte[] body = response.getBody();
            if(body != null) { dos.write(body, 0, body.length); }
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
