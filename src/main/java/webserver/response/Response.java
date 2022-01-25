package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final DataOutputStream dos;
    private final String url;

    public Response(OutputStream out, String url){
        this.dos = new DataOutputStream(out);
        this.url = url;
    }

    public void write() throws IOException {
        String mimeType = IOUtils.readMimeType(url);
        byte[] body = Files.readAllBytes(new File(url).toPath());
        response200Header(body.length, mimeType);
        responseBody(body);
    }

    private void response200Header(int lengthOfBodyContent, String mimeType) throws IOException{
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + mimeType +";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
