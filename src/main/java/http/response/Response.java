package http.response;

import http.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;
import view.ViewRedirect;
import view.ViewRender;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final DataOutputStream dos;
    private final View view;
    private final HttpStatusCode statusCode;

    public Response(DataOutputStream dos, View view){
        this.dos = dos;
        this.view = view;
        this.statusCode = view.getStatusCode();
    }


    public void write() throws IOException {
        log.info("HTTP STATUS : " + statusCode.getStatusCode() + ", " + statusCode.getMessage());
        writeStatus();
    }

    private void writeStatus() throws IOException {
        dos.writeBytes("HTTP/1.1 "+ statusCode.getStatusCode() + " " + statusCode.getMessage() + "\r\n");
        if(statusCode == HttpStatusCode.SUCCESS){
            writeSuccess();
        }
        if(statusCode == HttpStatusCode.REDIRECT){
            writeRedirect();
        }
        dos.flush();
    }

    private void writeSuccess() throws IOException {
        ViewRender viewRender = (ViewRender) view;
        byte[] body = viewRender.render();

        dos.writeBytes("Content-Type: " + viewRender.getMimeType() +";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");

        dos.write(body, 0, body.length);
    }

    private void writeRedirect() throws IOException {
        ViewRedirect viewRedirect = (ViewRedirect) view;
        dos.writeBytes("Location: "+ viewRedirect.getUrl() + "\r\n");
        if(viewRedirect.login()){
            dos.writeBytes("Set-Cookie: logined=" + viewRedirect.login() + "; Path=/\r\n");
        }
    }
}
