package webserver.writer;

import http.HttpBody;
import http.HttpHeaders;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResponseWriter {

    private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    public static void write(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getProtocolVersion() + " " + response.getStatusCode() + " " + response.getStatusText() + "\r\n");
            HttpHeaders headers = response.getHeaders();
            for(Map.Entry<String, List<String>> header : headers.getHeaders().entrySet()) {
                String valueString = header.getValue().stream().collect(Collectors.joining(","));
                dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), valueString));
            }
            dos.writeBytes("\r\n");

            HttpBody httpBody = response.getBody();
            if(!Objects.isNull(httpBody)) {
                byte[] data = httpBody.getBody();
                if(data != null) { dos.write(data, 0, data.length); }
            }
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
