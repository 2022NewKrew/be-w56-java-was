package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    private String path;
    private Map<String, String> cookie = new HashMap<>();

    public void setPath(String path) {
        this.path = path;
    }

    public void setCookie(String key, String value){
        this.cookie.put(key, value);
    }

    public void ok() {
        try {
            byte[] body = getHtmlBytes(path);
            dos.writeBytes(Status.OK.getMessage());
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void redirect(String redirectPath) {
        try {
            dos.writeBytes(Status.FOUND.getMessage());
            dos.writeBytes("Location: " + redirectPath+"\r\n");
            if(!cookie.isEmpty()){
                dos.writeBytes(makeCookie());
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String makeCookie(){
        StringBuilder result = new StringBuilder();
        result.append("Set-Cookie: ");
        cookie.forEach((key,value)->{
            result.append(key).append("=").append(value).append(";");
        });
        result.append("Path=/\r\n");
        return result.toString();
    }

    private byte[] getHtmlBytes(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
