package http.response;

import http.HttpStatusCode;
import http.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class Response {
    private static final String ROOT_URL = "http://localhost:8080";
    private static final String ROOT_DIRECTORY = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final DataOutputStream dos;
    private final HttpStatusCode statusCode;
    private final String url;

    public Response(OutputStream out, ResponseData responseData){
        this.dos = new DataOutputStream(out);
        this.statusCode = responseData.getStatusCode();
        this.url = responseData.getUrl();
    }

    public void write() throws IOException {
        if(statusCode.getStatusCode() == 302){
            responseRedirect();
        }
        else{
            String mimeType = IOUtils.readMimeType(ROOT_DIRECTORY + url);
            byte[] body = Files.readAllBytes(new File(ROOT_DIRECTORY + url).toPath());
            responseHeader(body.length, mimeType);
            responseBody(body);
        }
    }

    private void responseHeader(int lengthOfBodyContent, String mimeType) throws IOException{
        dos.writeBytes("HTTP/1.1 "+ statusCode.getStatusCode() +" "+ statusCode.getMessage() +"\r\n");
        dos.writeBytes("Content-Type: " + mimeType +";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseRedirect() throws IOException{
        log.info("REDIRECT");
        dos.writeBytes("HTTP/1.1 "+ statusCode.getStatusCode() + " " + statusCode.getMessage() + "\r\n");
        dos.writeBytes("Location: "+ ROOT_URL + url + "\r\n");
    }

    private void responseBody(byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
