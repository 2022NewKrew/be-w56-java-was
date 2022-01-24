package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class MyHttpResponse {
    private static final Logger log = LoggerFactory.getLogger(MyHttpResponse.class);
    private static final String DEFAULT_VERSION = "HTTP/1.1";
    private static final String DEFAULT_STATUS = "200 OK";
    private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String CRLF = "\r\n";

    private final DataOutputStream dos;
    private final String version;
    private final String status;
    private final String contentType;
    private final int contentLength;
    private final byte[] body;

    private MyHttpResponse(Builder builder){
        this.dos = builder.dos;
        this.version = builder.version;
        this.status = builder.status;
        this.contentType = builder.contentType;
        this.contentLength = builder.body.length;
        this.body = builder.body;
    }
    public static Builder builder(DataOutputStream dos){
        return new Builder(dos);
    }
    public void writeBytes(){
        try {
            dos.writeBytes(String.format("%s %s %s", version, status, CRLF));
            dos.writeBytes(String.format("Content-Type: %s%s", contentType, CRLF));
            dos.writeBytes(String.format("Content-Length: %s%s", contentLength, CRLF));
            dos.writeBytes(CRLF);
            dos.write(body, 0, contentLength);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static class Builder {
        private final DataOutputStream dos;
        private String version = DEFAULT_VERSION;
        private String status = DEFAULT_STATUS;
        private String contentType = DEFAULT_CONTENT_TYPE;
        private byte[] body;

        //Builder class
        public Builder(DataOutputStream dos) {
            this.dos = dos;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }
        public MyHttpResponse build() {
            return new MyHttpResponse(this);
        }
    }
}
