package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final DataOutputStream dos;
    private final byte[] body;
    private final String version;
    private final HttpStatus status;
    private final Map<String, String> headers;
    private final String contentType;
    private final int lengthOfBodyContent;

    public static class Builder {
        private final DataOutputStream dos;
        private final Map<String, String> headers = new HashMap<>();

        private byte[] body = new byte[]{};
        private String version = "HTTP/1.1";
        private HttpStatus status = HttpStatus.OK;
        private String contentType = "";
        private int lengthOfBodyContent = 0;

        public Builder(DataOutputStream dos) {
            this.dos = dos;
        }

        public Builder body(byte[] body){
            this.body = body;
            this.lengthOfBodyContent = body.length;
            return this;
        }
        public Builder version(String version){
            this.version = version;
            return this;
        }
        public Builder status(HttpStatus status){
            this.status = status;
            return this;
        }
        public Builder headers(String key, String value){
            headers.put(key, value);
            return this;
        }
        public Builder contentType(String contentType){
            this.contentType = contentType;
            return this;
        }

        public Response build(){
            return new Response(this);
        }
    }

    private Response(Builder builder){
        this.dos = builder.dos;
        this.body = builder.body;
        this.version = builder.version;
        this.status = builder.status;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.lengthOfBodyContent = builder.lengthOfBodyContent;
    }

    public void write() {
        try {
            dos.writeBytes(String.format("%s %s \r\n", version, status));
            writeBytesHeaders();
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
            dos.writeBytes(String.format("Content-Length: %d \r\n", lengthOfBodyContent));
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void writeBytesHeaders() throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(String.format("%s: %s \r\n", key, headers.get(key)));
        }
    }

    @Override
    public String toString() {
        return "Response is : " + dos.toString()
                + version
                + status.toString()
                + headers
                + contentType
                + lengthOfBodyContent;
    }
}
