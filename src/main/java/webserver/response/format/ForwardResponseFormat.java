package webserver.response.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.response.ResponseCode;
import webserver.response.ResponseFile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ForwardResponseFormat implements ResponseFormat {
    private static final Logger log = LoggerFactory.getLogger(ForwardResponseFormat.class);

    private DataOutputStream dos;
    private String cookie;
    private ResponseFile responseFile;
    private String htmlPage;

    public ForwardResponseFormat(OutputStream os) {
        this.dos = new DataOutputStream(os);
    }

    public ForwardResponseFormat(OutputStream os, String filePath) {
        this.dos = new DataOutputStream(os);
        this.responseFile = new ResponseFile(filePath);
    }

    public void setHtmlPage (String htmlPage) {
        this.htmlPage = htmlPage;
    }

    public void setCookie (String key, String value) {
        this.cookie = key+"="+value+"; Path=/";
    }

    @Override
    public void sendResponse (ResponseCode status) {
        byte[] body = htmlPage == null ? responseFile.getFileBytes() : htmlPage.getBytes();
        responseHeader(status, body.length);
        responseBody(body);
    }

    private void responseHeader(ResponseCode status, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 "+status.getStatusCode()+" "+status.getStatusName()+"\r\n");
            dos.writeBytes("Content-Length: "+lengthOfBodyContent+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    protected void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
