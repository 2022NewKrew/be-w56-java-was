package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final DataOutputStream dos;
    private final Map<String,String> cookies = new HashMap<>();

    public Response(OutputStream out) throws IOException {
        this.dos = new DataOutputStream(out);
    }

    public void writeHeader(int lengthOfBodyContent, int status) throws IOException {
        dos.writeBytes("HTTP/1.1 "+Integer.toString(status)+" OK \r\n");
        //dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        writeCookies(cookies);
        dos.writeBytes("\r\n");
    }

    public void writeRedirectHeader(String redirectUri) throws IOException {
        dos.writeBytes("HTTP/1.1 302 OK \r\n");
        dos.writeBytes("Location: " + redirectUri + "\r\n");
        writeCookies(cookies);
        dos.writeBytes("\r\n");
    }

    public void writeBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void setCookie(String key, String value){
        cookies.put(key, value);
    }

    private void writeCookies(Map<String,String> cookies) throws IOException {
        StringBuilder cookieString = new StringBuilder();
        for(String key : cookies.keySet()){
            cookieString.append("Set-Cookie: ").append(key).append("=").append(cookies.get(key)).append("\r\n");
        }
        dos.writeBytes(cookieString.toString());
    }
}
