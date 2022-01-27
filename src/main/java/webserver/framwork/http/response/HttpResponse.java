package webserver.framwork.http.response;

import webserver.framwork.http.Header;

public class HttpResponse {
    private HttpStatus status = HttpStatus.Ok;
    private final Header header = new Header();
    private byte[] body = new byte[0];

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public Header getHeader() {
        return this.header;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
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

    public void setBody(byte[] body) {
        this.body = body;
    }

}
