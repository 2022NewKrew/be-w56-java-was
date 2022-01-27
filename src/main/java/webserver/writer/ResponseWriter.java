package webserver.writer;

import http.HttpHeaders;
import http.response.HttpResponse;
import http.response.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ResponseWriter {

    private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    public static void write(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getProtocolVersion() + " " + response.getStatusCode() + " " + response.getStatusText() + "\r\n");
            HttpHeaders headers = response.getHeaders();
            for(Map.Entry<String, String> header : headers.getHeaders().entrySet()) {
                dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
            }
            dos.writeBytes("\r\n");

            ResponseBody responseBody = response.getBody();
            if(!Objects.isNull(responseBody)) {
                byte[] body = responseBody.getBody();
                if(body != null) { dos.write(body, 0, body.length); }
            }
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
