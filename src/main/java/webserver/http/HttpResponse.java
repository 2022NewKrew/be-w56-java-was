package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpStatus status;
    private String url;
    private Map<String, String> headers;
    private byte[] body = new byte[0];

    public HttpResponse(HttpStatus status){
        this.status = status;
        this.url = "/";
        this.headers = new HashMap<>();
    }

    public HttpResponse(HttpStatus status, String url){
        this(status);
        this.url = url;
    }

    public HttpResponse(HttpStatus status, String url, Map<String, String> headers) {
        this(status, url);
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setHeader(String key, String value){
        headers.put(key, value);
    }

    public void send(DataOutputStream dos) throws IOException {
        responseSetHeader(dos, body.length);
        responseSetBody(dos, body);
    }

    private void responseSetHeader(DataOutputStream dos, int lengthOfBodyContent) throws IOException{
        dos.writeBytes("HTTP/1.1 " + status + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        for (String key : headers.keySet()) {
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void responseSetBody(DataOutputStream dos, byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
