package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final DataOutputStream dos;

    public Response(OutputStream out) throws IOException {
        this.dos = new DataOutputStream(out);
    }

    public void writeHeader(int lengthOfBodyContent, int status) throws IOException {
        dos.writeBytes("HTTP/1.1 "+Integer.toString(status)+" OK \r\n");
        //dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    public void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
