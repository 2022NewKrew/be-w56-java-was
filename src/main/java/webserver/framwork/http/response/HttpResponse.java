package webserver.framwork.http.response;

import webserver.framwork.http.Header;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final Header header = new Header();
    private HttpStatus status = HttpStatus.Ok;
    private byte[] body = new byte[0];

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    public void setBody(byte[] body){
        this.body = body;
    }

    public Header getHeader() {
        return this.header;
    }

    public void setCookie(String key, String value) {
        this.addHeaderValue("Set-Cookie", key + "=" + value + "; Path=/");
    }

    public void addHeaderValue(String key, String value) {
        this.header.addValue(key, value);
    }

    public String getHeaderValue(String key) {
        return this.header.getValue(key);
    }

}
