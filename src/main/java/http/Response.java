package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private String path;
    private DataOutputStream dos;
    private byte[] body;

    public Response(String path, OutputStream out) throws IOException {
        this.dos = new DataOutputStream(out);
        this.path = path;

        //redirect
        if(path.startsWith("redirect:")){
            path = path.substring("redirect:".length(), path.length());
            response302Header(dos, path);
            return;
        }

        this.body = Files.readAllBytes(new File("./webapp" + path).toPath());

        setHeader();
        setBody();
    }

    private void setHeader() {
        response200Header(dos, body.length);
    }


    private void setBody() throws IOException {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void send() throws IOException {
        dos.flush();
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + path + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
