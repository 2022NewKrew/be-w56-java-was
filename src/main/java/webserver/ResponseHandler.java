package webserver;

import lombok.Getter;
import org.apache.http.HttpStatus;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.ContentType;


import java.io.*;

import static webserver.utils.Constants.CONTEXT_PATH;

@Getter
public class ResponseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHandler.class);

    private String httpVersion;
    private int statusCode = HttpStatus.SC_NOT_FOUND;
    private long contentLength;
    private String connection = "close";
    private ContentType contentType;
    private byte[] body;

    public ResponseHandler(RequestHandler requestHandler) throws IOException {
        this.httpVersion = requestHandler.getHttpVersion();
        File file = new File(CONTEXT_PATH + requestHandler.getUrl());

        if (file.exists()) {
            statusCode = HttpStatus.SC_OK;
        }

        FileInputStream fis = new FileInputStream(file);
        contentLength = file.length();
        body = new byte[(int) file.length()];
        fis.read(body);
        fis.close();

        Tika tika = new Tika();
        String mime = tika.detect(file);
        contentType = ContentType.builder().mime(mime).build();
    }

    public void makeHeaderAndFlush(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);

        try {
            dos.writeBytes(httpVersion + " " + statusCode);
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            dos.writeBytes("Connection: " + connection + "\r\n");
            dos.writeBytes("Content-Type: " + contentType);
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
